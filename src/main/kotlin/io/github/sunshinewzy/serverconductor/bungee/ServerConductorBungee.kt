package io.github.sunshinewzy.serverconductor.bungee

import com.google.common.io.ByteStreams
import net.md_5.bungee.BungeeCord
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.event.PluginMessageEvent
import taboolib.common.platform.Plugin
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info
import taboolib.platform.util.bungeePlugin

object ServerConductorBungee : Plugin() {

    override fun onEnable() {
        bungeePlugin.proxy.registerChannel("server_conductor:switch")
        info("Successfully running ServerConductorBungee!")
    }
    
    @Suppress("UnstableApiUsage")
    @SubscribeEvent
    fun onPluginMessageReceive(event: PluginMessageEvent) {
        if (event.tag != "server_conductor:switch") return
        
        val data = ByteStreams.newDataInput(event.data)
        val player = BungeeCord.getInstance().getPlayer(event.receiver.toString())
        val server = data.readUTF()
        
        if (player.server.info.name.equals(server, true))
            return

        info("${player.name} (${player.uniqueId}) Switched from [${player.server.info.name}] to [$server]")
        val target = ProxyServer.getInstance().getServerInfo(server)
        player.connect(target)
    }

}