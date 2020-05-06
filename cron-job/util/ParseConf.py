# -*- coding:utf-8 -*-
import ConfigParser
import os

class ParseConf(object) :
    def __init__(self, _confFile):
        self.confFile = _confFile
        self.configP = ConfigParser.ConfigParser()
        self.configP.read(_confFile)

    def readSections(self):
        return self.configP.sections()

    def readOptions(self, _param):
        return self.configP.options(_param)

    def readItems(self, _param):
        return self.configP.items(_param)

    def readGet(self, _section, _option):
        return self.configP.get(_section, _option)

    def setOption(self, _section, _option, _value):
        self.configP.set(_section, _option, _value)
        self.configP.write(open(self.confFile, 'w'))
