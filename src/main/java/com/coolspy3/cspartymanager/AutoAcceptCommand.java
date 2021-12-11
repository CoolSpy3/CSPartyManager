package com.coolspy3.cspartymanager;

import com.coolspy3.csmodloader.network.SubscribeToPacketStream;
import com.coolspy3.csmodloader.util.Utils;
import com.coolspy3.cspackets.datatypes.MCColor;
import com.coolspy3.cspackets.packets.ClientChatSendPacket;
import com.coolspy3.util.ModUtil;

public class AutoAcceptCommand
{

    private final int addLength = "/apa add ".length();
    private final int removeLength = "/apa remove ".length();

    @SubscribeToPacketStream
    public boolean register(ClientChatSendPacket event)
    {
        String msg = event.msg;
        if (msg.startsWith("/apa "))
        {
            if (msg.startsWith("/apa add "))
            {
                if (msg.matches("/apa add [a-zA-Z0-9_]+"))
                {
                    String player = msg.substring(addLength).toLowerCase();
                    if (!Config.getInstance().autoAcceptedPlayers.contains(player))
                    {
                        Config.getInstance().autoAcceptedPlayers.add(player);
                    }
                    ModUtil.sendMessage(MCColor.AQUA + "Auto Accepting Party Requests From: \""
                            + player + "\"");
                    Utils.reporting(Config::save);
                }
                else
                {
                    ModUtil.sendMessage(MCColor.RED + "Invalid Username: \""
                            + msg.substring(addLength).toLowerCase() + "\"");
                }
            }
            else if (msg.startsWith("/apa remove "))
            {
                if (msg.matches("/apa remove [a-zA-Z0-9_]+"))
                {
                    String player = msg.substring(removeLength).toLowerCase();
                    if (Config.getInstance().autoAcceptedPlayers.contains(player))
                    {
                        Config.getInstance().autoAcceptedPlayers.remove(player);
                    }
                    ModUtil.sendMessage(MCColor.RED
                            + "No Longer Auto Accepting Party Requests From: \"" + player + "\"");
                    Utils.reporting(Config::save);
                }
                else
                {
                    ModUtil.sendMessage(MCColor.RED + "Invalid Username: \""
                            + msg.substring(removeLength).toLowerCase() + "\"");
                }
            }
            else if (msg.startsWith("/apa list"))
            {
                ModUtil.sendMessage(MCColor.AQUA + "Auto Accepting Party Requests From:");
                if (Config.getInstance().autoAcceptedPlayers.size() == 0)
                {
                    ModUtil.sendMessage(MCColor.AQUA + "<Nobody>");
                }
                else
                {
                    for (String player : Config.getInstance().autoAcceptedPlayers)
                    {
                        ModUtil.sendMessage(MCColor.AQUA + player);
                    }
                }
            }
            else
            {
                ModUtil.sendMessage(MCColor.RED + "Usage: /apa [add | remove | list] <player>");
            }

            return true;
        }
        else if (msg.equals("/apa"))
        {
            Config.getInstance().autoAcceptEnabled = !Config.getInstance().autoAcceptEnabled;
            if (Config.getInstance().autoAcceptEnabled)
            {
                ModUtil.sendMessage(MCColor.AQUA + "Auto Party Accept Enabled!");
            }
            else
            {
                ModUtil.sendMessage(MCColor.RED + "Auto Party Accept Disabled!");
            }
            Utils.reporting(Config::save);

            return true;
        }

        return false;
    }

}
