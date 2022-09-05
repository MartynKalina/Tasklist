fun main() {
    var input1 = readLine()!!
    val firstChard = input1[0]
    val lastChar = input1[input1.length - 1]
    println(input1.replaceRange(0,1, lastChar.toString()).replaceRange(input1.length - 1,input1.length,firstChard.toString()))
}