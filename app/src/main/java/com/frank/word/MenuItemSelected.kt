package com.frank.word

fun showHelp() {
    isShowEditText = true

    inputText = """文字末尾打入字符时的隐藏功能

换行：
  有文字时检查正确性
     正确时清空, 错误时显示正确单词
  无文字时：
     下一个单词

<: 上一个单词
空格或点击单词：清空

调整时刻模式,顺序,全部播放，才可以的:
  +：插入新单词
  -：删除当前单词(删文字 留读音)(慎用！！)
  !；设置当前单词为<非单词>属性(文字读音全删)

#：更新当前单词
&：查找并移动到指定单词位置
*：在所有书中，查找单词
@：调整单词播放时刻 切换
%：修改词性
(：修改音调
)：编辑单词文件
$：显示分割中日文间隔按钮
9：添加，修改例句1
?：显示本帮助信息。

"""

}

fun editWordFile() {
    if (lrcFile != null) {
        isLRC_Time_OK = false
        val str = readRawTxtFile(lrcFile!!)
        editWords(str)
    }
}