package co.minehub.changeannouncer;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by portalBlock on 10/16/2014.
 * **This plugin is made for and property of Minehub.co, unauthorized use is strictly prohibited!**
 */
public class ChangeAnnouncer extends Plugin implements Listener{

    //Start Config
    private final String JOIN = "&7%s &ajoined &3Minehub Network";
    private final String CHANGE = "&7%s &aswitched to &b%s";
    private final String LEAVE = "&c%s &4&oтев &3Minehub Network";
    //End Config

    //Don't edit.
    private final List<UUID> JUST_JOINED = new ArrayList<UUID>();

    @Override
    public void onEnable(){
        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onConnect(PostLoginEvent e){
        getProxy().broadcast(makeMsg(JOIN, e.getPlayer().getDisplayName()));
        //Add to list because the {@link ServerConnectedEvent} needs to check if the player just joined.
        JUST_JOINED.add(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onChange(ServerConnectedEvent e){
        //Do check here because this event is also fired when someone connects to the lobby(don't want duplicate messages).
        if(!JUST_JOINED.contains(e.getPlayer().getUniqueId())){
            getProxy().broadcast(makeMsg(CHANGE, e.getPlayer().getDisplayName(), e.getServer().getInfo().getName()));
        }else{
            JUST_JOINED.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e){
        getProxy().broadcast(makeMsg(LEAVE, e.getPlayer().getDisplayName()));
    }

    private BaseComponent[] makeMsg(String s, String... params){
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', String.format(s, params)));
    }

}
