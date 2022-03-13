package com.exmple.subtitleparser2
import java.util.*

object StrParser {
    fun parse(strFileContent: String): ArrayList<Str> {
        val stre:String
        val list = ArrayList<Str>()
        val lines = strFileContent.lines()
        val tempList = ArrayList<String>()

        for (line in lines) {
            if (line.trim() != "")
                tempList.add(line)
            else {
                if (tempList.size >= 3) {
                    val num = tempList[0]
                    val times = tempList[1].split(" --> ")
                    var text = ""
                    for (i in 2 until tempList.size) {
                        text += tempList[i] + "\n"
                    }

                    val str = Str(num, times[0], times[1], text.trim())
                    list.add(str)
                    tempList.clear()
                }
            }
        }
        return list
    }
}