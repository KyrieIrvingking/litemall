package org.linlinjava.litemall.core.qcode;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import me.chanjar.weixin.common.error.WxErrorException;
import org.linlinjava.litemall.core.storage.StorageService;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class QCodeService {
    private static final String INVITE_LOGO_NAME="invite_back_logo2.jpg";
    private static final String INVITE_BACK__NAME="back.png";
    private static final String INVITE_ADDRESS="storage/fetch/invite/posterImg/";
    private static final String INVITE_STORAGE__PATH="storage/invite/posterImg";
    @Autowired
    WxMaService wxMaService;

    @Autowired
    private StorageService storageService;
    @Autowired
    private LitemallUserService litemallUserService;

    /**
     * 创建商品分享图
     *
     * @param goodId
     * @param goodPicUrl
     * @param goodName
     */
    public String createGoodShareImage(String goodId, String goodPicUrl, String goodName) {

        try {
            //创建该商品的二维码
            File file = wxMaService.getQrcodeService().createWxaCodeUnlimit("goods," + goodId, "pages/index/index");
            FileInputStream inputStream = new FileInputStream(file);
            //将商品图片，商品名字,商城名字画到模版图中
            byte[] imageData = drawPicture(inputStream, goodPicUrl, goodName);
            ByteArrayInputStream inputStream2 = new ByteArrayInputStream(imageData);
            //存储分享图
            LitemallStorage litemallStorage = storageService.store(inputStream2, imageData.length, "image/jpeg", getKeyName(goodId));

            return litemallStorage.getUrl();
        } catch (WxErrorException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String getKeyName(String goodId) {
        return "GOOD_QCODE_" + goodId + ".jpg";
    }

    /**
     * 将商品图片，商品名字画到模版图中
     *
     * @param qrCodeImg  二维码图片
     * @param goodPicUrl 商品图片地址
     * @param goodName   商品名称
     * @return
     * @throws IOException
     */
    private byte[] drawPicture(InputStream qrCodeImg, String goodPicUrl, String goodName) throws IOException {
        //底图
        ClassPathResource redResource = new ClassPathResource(INVITE_BACK__NAME);
        BufferedImage red = ImageIO.read(redResource.getInputStream());


        //商品图片
        URL goodPic = new URL(goodPicUrl);
        BufferedImage goodImage = ImageIO.read(goodPic);

        //小程序二维码
        BufferedImage qrCodeImage = ImageIO.read(qrCodeImg);

        // --- 画图 ---

        //底层空白 bufferedImage
        BufferedImage baseImage = new BufferedImage(red.getWidth(), red.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);

        //画上图片
        drawImgInImg(baseImage, red, 0, 0, red.getWidth(), red.getHeight());

        //画上商品图片
        drawImgInImg(baseImage, goodImage, 71, 69, 660, 660);

        //画上小程序二维码
        drawImgInImg(baseImage, qrCodeImage, 448, 767, 300, 300);

        //写上商品名称
        drawTextInImg(baseImage, goodName, 65, 867);

        //写上商城名称
//        drawTextInImgCenter(baseImage, shopName, 98);


        //转jpg
        BufferedImage result = new BufferedImage(baseImage.getWidth(), baseImage
                .getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        result.getGraphics().drawImage(baseImage, 0, 0, null);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageIO.write(result, "jpg", bs);

        //最终byte数组
        return bs.toByteArray();
    }

    private void drawTextInImgCenter(BufferedImage baseImage, String textToWrite, int y) {
        Graphics2D g2D = (Graphics2D) baseImage.getGraphics();
        g2D.setColor(new Color(167, 136, 69));

        String fontName = "Microsoft YaHei";

        Font f = new Font(fontName, Font.PLAIN, 28);
        g2D.setFont(f);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 计算文字长度，计算居中的x点坐标
        FontMetrics fm = g2D.getFontMetrics(f);
        int textWidth = fm.stringWidth(textToWrite);
        int widthX = (baseImage.getWidth() - textWidth) / 2;
        // 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。

        g2D.drawString(textToWrite, widthX, y);
        // 释放对象
        g2D.dispose();
    }

    private void drawTextInImg(BufferedImage baseImage, String textToWrite, int x, int y) {
        Graphics2D g2D = (Graphics2D) baseImage.getGraphics();
        g2D.setColor(new Color(167, 136, 69));

        //TODO 注意，这里的字体必须安装在服务器上
        g2D.setFont(new Font("Microsoft YaHei", Font.PLAIN, 20));
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2D.drawString(textToWrite, x, y);
        g2D.dispose();
    }

    private void drawImgInImg(BufferedImage baseImage, BufferedImage imageToWrite, int x, int y, int width, int heigth) {
        Graphics2D g2D = (Graphics2D) baseImage.getGraphics();
        g2D.drawImage(imageToWrite, x, y, width, heigth, null);
        g2D.dispose();
    }

    /**
     * 生成邀请海报
     * @param url
     * @return
     */
    public String createWxInviteImage(String url,Integer userId,String inviteLogoUrl) {
        try {
            //获取用户信息
            LitemallUser user = litemallUserService.findById(userId);
           /* String userImageUrl = user.getAvatar();
            String nickName =user.getNickname();*/
            String userImageUrl = "https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83erOOCI2Siame09nz0MYQK441O19n6gg1YwKPraEyHmSkxGIWQ9WsKmGUyuGqfyQbdtLoqqDYox5YhQ/132";
            String nickName ="lcy";
            //创建该商品的二维码
            File file = wxMaService.getQrcodeService().createWxaCodeUnlimit(userId+"",url);
            FileInputStream inputStream = new FileInputStream(file);
            //将商品图片，商品名字,商城名字画到模版图中
            byte[] imageData = drawPictureNew(inputStream, userImageUrl,
                    inviteLogoUrl,  nickName+"邀您上小绿券享京东优惠");
            ByteArrayInputStream inputStream2 = new ByteArrayInputStream(imageData);

            //存储分享图
            LitemallStorage litemallStorage = storageService.store(inputStream2, imageData.length, "image/jpeg", getKeyName("invite-image"));

            return litemallStorage.getUrl();
        } catch (WxErrorException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 将商品图片，商品名字画到模版图中
     *
     * @param qrCodeImg  二维码图片
     * @param goodName   商品名称
     * @return
     * @throws IOException
     */
    private byte[] drawPictureNew(InputStream qrCodeImg,String userPicUrl, String inviteLogoUrl, String goodName) throws IOException {
        //底图
        ClassPathResource redResource = new ClassPathResource(INVITE_BACK__NAME);
        BufferedImage red = ImageIO.read(redResource.getInputStream());

        //用户头像图片
        URL userPic = new URL(userPicUrl);
        BufferedImage userImage = ImageIO.read(userPic);

        //logo图片
        URL inviteLogoPic = new URL(inviteLogoUrl);
        BufferedImage inviteLogoImage = ImageIO.read(inviteLogoPic);

        //小程序二维码
        BufferedImage qrCodeImage = ImageIO.read(qrCodeImg);

        // --- 画图 ---

        //底层空白 bufferedImage
        BufferedImage baseImage = new BufferedImage(red.getWidth(), red.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);

        //画上图片
        drawImgInImg(baseImage, red, 0, 0, red.getWidth(), red.getHeight());

        //画上商品图片
        drawImgInImg(baseImage, inviteLogoImage, 71, 69, 660, 660);

        //画上个人头像
        drawImgInImg(baseImage, userImage, 80, 900, 120, 120);

        //画上小程序二维码
        drawImgInImg(baseImage, qrCodeImage, 448, 767, 300, 300);

        //写上商品名称
        drawTextInImg(baseImage, goodName, 65, 867);

        //写上商城名称
//        drawTextInImgCenter(baseImage, shopName, 98);


        //转jpg
        BufferedImage result = new BufferedImage(baseImage.getWidth(), baseImage
                .getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        result.getGraphics().drawImage(baseImage, 0, 0, null);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageIO.write(result, "jpg", bs);

        //最终byte数组
        return bs.toByteArray();
    }

    /**
     * 生成小程序二维码图片
     * @param url
     * @return
     */
    public String createWxCodeImage(String url,String token) {
        try {
            File file = wxMaService.getQrcodeService().createWxaCodeUnlimit(token,url);
            FileInputStream qrCodeImg = new FileInputStream(file);
            //小程序二维码
            BufferedImage qrCodeImage = ImageIO.read(qrCodeImg);

            //转jpg
            BufferedImage result = new BufferedImage(qrCodeImage.getWidth(), qrCodeImage
                    .getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            result.getGraphics().drawImage(qrCodeImage, 0, 0, null);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(result, "jpg", bs);

            ByteArrayInputStream inputStream2 = new ByteArrayInputStream(bs.toByteArray());
            //存储分享图
            LitemallStorage litemallStorage = storageService.store(inputStream2, bs.toByteArray().length, "image/jpeg", getKeyName("wx-code"));

            return litemallStorage.getUrl();
        } catch (WxErrorException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 生成二维码
     * @param url
     * @param code
     * @return
     */
    public String createUrlImage(String url,String code)
    {
        try {
            //创建该商品的二维码
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 300, 300, hints);

            BufferedImage qrCodeImage = toBufferedImage(bitMatrix);
            //logo图片
            ClassPathResource redResource = new ClassPathResource("jd_logo.jpg");
            BufferedImage logoImage = ImageIO.read(redResource.getInputStream());

            //二维码添加logo
            LogoConfig logoConfig = new LogoConfig();
            addLogo(qrCodeImage,logoImage,logoConfig);

            //转jpg
            BufferedImage result = new BufferedImage(qrCodeImage.getWidth(), qrCodeImage
                    .getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            result.getGraphics().drawImage(qrCodeImage, 0, 0, null);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(result, "jpg", bs);

            ByteArrayInputStream inputStream2 = new ByteArrayInputStream(bs.toByteArray());
            //存储分享图
            LitemallStorage litemallStorage = storageService.store(inputStream2, bs.toByteArray().length, "image/jpeg", getKeyName(code));

            return litemallStorage.getUrl();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }


    private static void addLogo(BufferedImage qrImage, BufferedImage logoImage, LogoConfig logoConfig) {

        try {

            // 1、读取二维码图片，并构建绘图对象
            Graphics2D graph = qrImage.createGraphics();

            int widthLogo = qrImage.getWidth() / logoConfig.getLogoPart();
            int heightLogo = qrImage.getHeight() / logoConfig.getLogoPart();

            // 3、计算图片放置的位置
            int x = (qrImage.getWidth() - widthLogo) / 2;
            int y = (qrImage.getHeight() - heightLogo) / 2;

            // 4、绘制图片
            graph.drawImage(logoImage, x, y, widthLogo, heightLogo, null);
            graph.drawRoundRect(x, y, widthLogo, heightLogo, 10, 10);
            graph.setStroke(new BasicStroke(logoConfig.getBorder()));
            graph.setColor(logoConfig.getBorderColor());
            graph.drawRect(x, y, widthLogo, heightLogo);
            graph.dispose();

        } catch (Exception e) {
            System.out.println(e);

        }
    }
}
