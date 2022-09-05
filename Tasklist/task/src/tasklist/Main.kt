package tasklist

import kotlinx.datetime.*
import kotlin.system.exitProcess
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

fun main() {



    val taskManager = TaskManager()

    taskManager.mainMenu()


}

class TaskManager{
    var input: String = "empty"
    private val jsonFile = File("tasklist.json")
    var inputTasks: MutableList<Task> = emptyList<Task>().toMutableList()

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val type = Types.newParameterizedType(List::class.java, Task::class.java)
    val taskListAdapter = moshi.adapter<List<Task?>>(type)

        init {
            if(jsonFile.exists()){
                val lines = jsonFile.readText()
                val taskList = taskListAdapter.fromJson(lines)
                if (taskList != null) {
                    for (item in taskList){
                        if (item != null) {
                            inputTasks.add(item)
                        }
                    }
                }
            }

           /* inputTasks.add(Task("C","2111-01-01","01:01","aaaaaa", mutableListOf("jasdasd")))
            inputTasks.add(Task("C","2111-01-01","01:01","bbbbbbbbbbb",mutableListOf("bbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbccccccccccccccccccccccc","bbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbccccccccccccccccccccccc","bbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbccccccccccccccccccccccc")))
            inputTasks.add(Task("C","2111-01-01","01:01","bbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbcccccccccccccccccccccccbbbbbccccccccccccccccccccccc",null))
            inputTasks.add(Task("C","2111-01-01","01:01","bbbbbdddddddddddddddddddddddddddddd",null))
            inputTasks.add(Task("C","2111-01-01","01:01","bbbbb",null))
            inputTasks.add(Task("C","2111-01-01","01:01","bbbbb",null))
            inputTasks.add(Task("C","2111-01-01","01:01","bbbbbdddddddddddddddddddddddddddd",null))
            inputTasks.add(Task("C","2111-01-01","01:01","bbbbb",null))
            inputTasks.add(Task("C","2111-01-01","01:01","bbbbb",null))
            inputTasks.add(Task("C","2111-01-01","01:01","bbbbb",null))
            inputTasks.add(Task("C","2111-01-01","01:01","bbbbb",mutableListOf("jasdasd","asdasdasd","asdasdasd")))
            inputTasks.add(Task("C","2111-01-01","01:01","bbbbb",null))*/
        }

    fun mainMenu(){
        println("Input an action (add, print, edit, delete, end):")
        while(input.isNotEmpty()){

            input = readLine()!!

            when (input) {
                "add" -> {
                    addTask()
                    mainMenu()
                }
                "print" -> {
                    printTask()
                    mainMenu()
                }
                "edit" -> {
                    editTask()
                    mainMenu()
                }
                "delete" -> {
                    deleteTask()
                    mainMenu()
                }
                "end" -> endTask()
                else -> {
                    println("The input action is invalid")
                    input = "something"
                    mainMenu()
                }
            }



        }

    }

    private fun deleteTask() {
       printTask()

        var temp = true
        var taskId = 0
        while (temp){
            println("Input the task number (1-${inputTasks.size}):")
            try {
                taskId = readLine()!!.trimEnd().trimStart().toInt()
                if (taskId in 1..inputTasks.size){
                    inputTasks.removeAt(taskId.toInt() - 1 )
                    println("The task is deleted")
                    temp = false
                }else{
                    println("Invalid task number")
                }
            }catch (e: NumberFormatException){
                println("Invalid task number")
            }
        }
    }

    private fun editTask() {
        printTask()

        var temp = true
        var temp2 = true

        while (temp){
            println("Input the task number (1-${inputTasks.size}):")

            try {
                val taskId = readLine()!!.trimEnd().trimStart().toInt()
                if (taskId in 1..inputTasks.size){
                    while (temp2){
                        println("Input a field to edit (priority, date, time, task):")
                        val editParam = readLine()!!.trimEnd().trimStart()
                        if (editParam == "priority" || editParam == "date" || editParam == "time" || editParam == "task" ){
                            when(editParam){
                                "priority" -> {
                                    inputTasks[taskId.toInt() - 1].priority = setPriority()
                                    temp2 = false
                                }
                                "date" -> {
                                    inputTasks[taskId.toInt() - 1].date =setDate()
                                    temp2 = false
                                }
                                "time" -> {
                                    inputTasks[taskId.toInt() - 1].time = setTime()
                                    temp2 = false
                                }
                                "task" -> {
                                    println("Input a new task (enter a blank line to end):")
                                    val newName = readLine()!!.trimEnd().trimStart()
                                    val nevim =  readLine()!!
                                    inputTasks[taskId.toInt() - 1].name = newName
                                    println("")
                                    temp2 = false
                                }
                            }

                        }else{
                            println("Invalid field")
                        }
                    }


                    println("The task is changed")
                    temp = false
                }else{
                    println("Invalid task number")
                }
            }catch (e: NumberFormatException){
                println("Invalid task number")
            }
        }
    }

    private fun endTask() {
        println("Tasklist exiting!")
        saveData(inputTasks)
        exitProcess(1)
    }

    private fun saveData(inputTasks: MutableList<Task>) {
        jsonFile.createNewFile()

        jsonFile.writeText(taskListAdapter.toJson(inputTasks))
        print(taskListAdapter.toJson(inputTasks))


    }

    private fun printTask() {
       // println("DEBUG: ${inputTasks.toString()}")

        if (inputTasks.isEmpty()){
            println("No tasks have been input")
            mainMenu()
        }else{
            println("+----+------------+-------+---+---+--------------------------------------------+\n" +
                    "| N  |    Date    | Time  | P | D |                   Task                     |\n" +
                    "+----+------------+-------+---+---+--------------------------------------------+")
            for (i in inputTasks.indices){

                var date = inputTasks[i].date.split("-")
                var time = inputTasks[i].time.split(":")
                var dueTo = "null"

                val taskDate = LocalDate(date[0].toInt(), date[1].toInt(), date[2].toInt())
                val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
                val numberOfDays = currentDate.daysUntil(taskDate)

                dueTo = if (numberOfDays > 0){
                    "I"
                }else if (numberOfDays < 0){
                    "O"
                }else{
                    "T"
                }
                if (i > 8){
                   print("| ${i+1} ")
                }else{
                    print("| ${i+1}  ")
                }
                print("| ${inputTasks[i].date} ")
                print("| ${inputTasks[i].time} ")

                when(inputTasks[i].priority.uppercase()){
                    "C" -> {
                        print("| \u001B[101m \u001B[0m ")
                    }
                    "H" -> {
                        print("| \u001B[103m \u001B[0m ")
                    }
                    "N" -> {
                        print("| \u001B[102m \u001B[0m ")
                    }
                    "L" -> {
                        print("| \u001B[104m \u001B[0m ")
                    }
                }

                when(dueTo){
                    "I" -> {
                        print("| \u001B[102m \u001B[0m ")
                    }
                    "T" -> {
                        print("| \u001B[103m \u001B[0m ")
                    }
                    else -> {
                        print("| \u001B[101m \u001B[0m ")
                    }
                }

                if(inputTasks[i].name.length<45){
                    print("|${inputTasks[i].name}")
                    repeat(44-inputTasks[i].name.length){
                        print(" ")
                    }
                    print("|")
                }else{
                    printLongString(inputTasks[i].name.toCharArray())
                }



                if (!(inputTasks[i].lines.isNullOrEmpty())){

                    for (line in inputTasks[i].lines!!){
                        if (line.toCharArray().size < 45){
                            print("\n")
                            print("|    |            |       |   |   |")
                            print(line)
                            repeat(44-line.toCharArray().size){
                                print(" ")
                            }
                            print("|")
                        }else{
                            print("\n")
                            print("|    |            |       |   |   ")
                            printLongString(line.toCharArray())
                        }
                    }
                }

                //lines!!

                print("\n")
                println("+----+------------+-------+---+---+--------------------------------------------+")

            }
        }
    }

    private fun printLongString(toCharArray: CharArray) {
        var stringArray = toCharArray
        var counter = 1

        print("|")
        for (item in stringArray.indices){
            if (counter < 45){
                print("${stringArray[item]}")
                counter++
            }else{
                print("|")
                print("\n")
                print("|    |            |       |   |   |")
                print("${stringArray[item]}")
                counter = 2
            }
        }
        repeat(45-counter){
            print(" ")
        }
        print("|")
    }

    private fun addTask() {
        var subtaskForAdd = "empty"
        val lines2 = mutableListOf<String>()

        val priority = setPriority()

        val date = setDate()

        val time = setTime()

        val name = setName()
        if (name.isEmpty()){
            println("The task is blank")
            mainMenu()
        }


        while(subtaskForAdd.isNotEmpty()){
            subtaskForAdd = readLine()!!.trimEnd().trimStart()
            if (subtaskForAdd.isEmpty()){
                inputTasks.add(Task(priority,date,time,name,lines2))
                mainMenu()
            }else{
                lines2.add(subtaskForAdd)
            }

        }


    }

    private fun setName(): String {
        println("Input a new task (enter a blank line to end):")
        val name = readLine()!!.trimEnd().trimStart()

        return name
    }

    private fun setTime(): String {
        var temp = true
        var result = "empty"
        var hour = "empty"
        var minute = "empty"

        while (temp){
            println("Input the time (hh:mm):")
            val time = readLine()!!.trimEnd().trimStart()
            val validator = time.split(":")
            if (validator.size == 2 && validator[0].toInt() in 0..23 && validator[1].toInt() in 0..59 ){
                hour = if (validator[0].toInt() in 0..9){
                    "0${validator[0].toInt()}"
                }else{
                    "${validator[0].toInt()}"
                }
                minute = if (validator[1].toInt() in 0..9){
                    "0${validator[1].toInt()}"
                }else{
                    "${validator[1].toInt()}"
                }
                result = "$hour:$minute"
                temp = false
            }else{
                println("The input time is invalid")
            }
        }
        return result
    }

    private fun setDate(): String  {
        var temp = true
        var result = "empty"

        while (temp){
            println("Input the date (yyyy-mm-dd):")
            val date = readLine()!!.trimEnd().trimStart()
            val validator = date.split("-")
            if (validator[0].length == 4 && validator.size == 3){
                try {
                    var validate = LocalDate(validator[0].toInt(),validator[1].toInt(),validator[2].toInt())
                    temp = false
                    result = validate.toString()
                } catch (e: IllegalArgumentException) {
                    temp = true
                    println("The input date is invalid")
                }
            }else{
                println("The input date is invalid")
            }
        }
        return result
    }

    private fun setPriority(): String {
        var temp = true
        var result = "empty"

        while (temp){
            println("Input the task priority (C, H, N, L):")
            val priority = readLine()!!.trimEnd().trimStart()
            if (priority.matches("[cChHnNlL]".toRegex())){
                result = priority
                temp = false
            }
        }
        return result
    }

}

data class Task(var priority: String, var date: String, var time: String, var name: String, var lines: MutableList<String>?)