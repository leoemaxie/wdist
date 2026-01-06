package org.wdist.com

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform