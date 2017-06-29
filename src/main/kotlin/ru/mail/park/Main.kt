package ru.mail.park

import java.lang.management.ManagementFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    var port = 80
    var root = "./"
    for (i in args.indices) {
        if ("-p" == args[i]) {
            port = args[i + 1].toInt()
            continue
        }
        if ("-r" == args[i]) {
            root = args[i + 1]
            continue
        }
        if ("-c" == args[i]) {
            setAffinity(args[i + 1].toInt())
        }
    }
    Server(port, root).start()
}

fun setAffinity(cores: Int) {
    if (cores <= 0) {
        println("Invalid concurrency value")
        return
    }
    val availableProcessors = Runtime.getRuntime().availableProcessors()
    println("Available processors: $availableProcessors")
    if (cores >= availableProcessors) {
        return
    }
    val systemName = System.getProperty("os.name")
    val pid = ManagementFactory.getRuntimeMXBean().getName().substringBefore('@').toInt()
    val mask = 1 shl cores - 1
    if (isWindows(systemName)) {
        Runtime.getRuntime().exec("cmd /c C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell \"\$Process = Get-Process -ID $pid; \$Process.ProcessorAffinity=$mask\"")
    } /*else if (isLinux(systemName)) {
        Runtime.getRuntime().exec("taskset -p $mask $pid")
        // JDK-6515172: Runtime.availableProcessors() ignores Linux taskset command
    } */else {
        println("Can not set affinity on your OS")
        return
    }
    println("Waiting for available processors value to change... ")
    while (Runtime.getRuntime().availableProcessors() != cores) {
        Thread.sleep(100)
    }
    println("Avaliable processors: $cores")
}

fun isWindows(systemName: String): Boolean = systemName.contains("win", true)

fun isLinux(systemName: String): Boolean = systemName.contains("nux", true)