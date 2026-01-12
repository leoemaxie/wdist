package org.wdist.com

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

fun getPlatform(): Platform = JVMPlatform()