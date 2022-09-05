fun main() {
    val string = readln()
    // write here
    var input = string.reversed().toCharArray()
    var index = 0

    for (i in input.indices){
      if (input[i] == 'u'){
          index = i - 1
          break
      }
    }
    for (y in 0..index){
        input[y]=input[y].uppercaseChar()
    }

    for (item in input.reversed()){
        print(item)
    }
}