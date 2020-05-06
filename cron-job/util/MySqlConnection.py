# -*- coding: UTF-8 -*-
import MySQLdb as mdb
import ParseConf
import sys,os
import logging
class MySqlConnection(object):
    """
        1.连接数据库
        2.查询select
        3.插入insert
    """
    global basePath
    basePath = "/home/xiaolvquan/xiaolvquan/cron-job"
    def __init__(self):
        logging.basicConfig(format='%(asctime)s - %(pathname)s[line:%(lineno)d] - %(levelname)s: %(message)s',level=logging.DEBUG)
        self.globalConfFile = "%s/conf/global.conf" % basePath
        parse = ParseConf.ParseConf(self.globalConfFile)
        self._host = parse.readGet("mysql", "host")
        self._port = int(parse.readGet("mysql", "port"))
        self._user = parse.readGet("mysql", "user")
        self._passWd = parse.readGet("mysql", "passwd")
        self._db = parse.readGet("mysql", "db")
        self._charset = 'utf8'
        self._con = None
        self._cur = None
        try:
            self._con = mdb.connect(
                host=self._host,
                port=self._port,
                user=self._user,
                passwd=self._passWd,
                db=self._db,
                charset=self._charset
            )
        except mdb.Error as e:
            logging.error("Error %d: %s" % (e.args[0], e.args[1]))
    
    def commit(self):
        if self._con is not None:
            try:
                self._con.ping()
            except:
                self.reconnect()
            try:
                self._con.commit()
            except Exception,e:
                self._con.rollback()
                logging.error("Can not commit",e)

    def rollback(self):
        if self._con is not None:
            try:
                self._con.rollback()
            except Exception,e:
                logging.error("Can not rollback")

    def reconnect(self):
        logging.info("Closes the existing database connection and re-opens it.")
        self.close()
        self._con = mdb.connect(
            host=self._host,
            port=self._port,
            user=self._user,
            passwd=self._passWd,
            db=self._db,
            charset=self._charset
        )
        self._con.autocommit(False)

    def _cursor(self):
        if self._con is None: 
            self.reconnect()
        try:
            self._con.ping()
        except:
            self.reconnect()
        return self._con.cursor()
    
    def close(self):
        """Closes this database connection."""
        if self._con is not None:
            self._con.close()
            self._con = None
        
    def select(self, sql):
        """
        封装select
        :param sql:
        :return: fetch_data
        """
        self._cur = self._cursor()
        try:
            self._cur.execute(sql)
            fetch_data = self._cur.fetchall()
            return fetch_data
        except self._cur.Error as e:
            logging.error("select error %d: %s" % (e.args[0], e.args[1]))
        finally:
            self._cur.close()
            self.close()

    def insert(self, sql):
        """
        封装insert
        :param sql:
        """
        rowcount = 0
        self._cur = self._cursor()
        try:
            self._cur.execute(sql)
            self.commit()
            rowcount = self._cur.rowcount
        except self._cur.Error as e:
            logging.error("insert error %d: %s" % (e.args[0], e.args[1]))
            self._con.rollback()
        finally:
            self._cur.close()
            self.close()
            return rowcount

    def insertBatch(self,table,cols,lists):
        colStr = ",".join(cols)
        values = ""
        rowcount = 0
        valueList = []
        for j, val in enumerate(lists):
            value = "(%s)" % ",".join(val)
            valueList.append(value)
            values = ",".join(valueList) 
        sql = "insert into %s (%s) values %s" % (table,colStr,values)
        logging.info(sql)
        self._cur = self._cursor() 
        try:
            print sql
            self._cur.execute(sql)
            self.commit()
            rowcount = self._cur.rowcount
        except self._cur.Error as e:
            logging.error("Error %d: %s" % (e.args[0], e.args[1]))
            self._con.rollback()
        finally:
            self._cur.close()
            self.close()
            return rowcount

    def delete(self, sql):
        """
        封装delete
        :param sql
        """
        self._cur = self._cursor()
        try:
           self._cur.execute(sql)
           self.commit()
        except:
           self._con.rollback()
        self._cur.close()
        self.close()

    def update(self, sql):
        """
        封装update
        """
        rowcount = 0
        self._cur = self._cursor()
        try:
            self._cur.execute(sql)
            self.commit()
            rowcount = self._cur.rowcount
        except self._cur.Error as e:
            logging.error("update error %d: %s" % (e.args[0], e.args[1]))
            self._con.rollback()
        finally:
            self._cur.close()
            self.close()
            return rowcount


