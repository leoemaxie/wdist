package org.wdist.com

class WasmPlatform: Platform {
    override val name: String = "WhyDidISaveThis"
}

actual fun getPlatform(): Platform = WasmPlatform()