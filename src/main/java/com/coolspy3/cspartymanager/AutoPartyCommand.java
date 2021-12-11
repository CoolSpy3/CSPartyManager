package com.coolspy3.cspartymanager;

import com.coolspy3.csmodloader.network.SubscribeToPacketStream;
import com.coolspy3.csmodloader.util.Utils;
import com.coolspy3.cspackets.datatypes.MCColor;
import com.coolspy3.cspackets.packets.ClientChatSendPacket;
import com.coolspy3.util.ModUtil;

public class AutoPartyCommand
{

    private final int addLength = "/ap add ".length();
    private final int removeLength = "/ap remove ".length();

    @SubscribeToPacketStream
    public boolean register(ClientChatSendPacket event)
    {
        String msg = event.msg;
        if (msg.startsWith("/ap "))
        {
            if (msg.startsWith("/ap add "))
            {
                if (msg.matches("/ap add [a-zA-Z0-9_]+"))
                {
                    String player = msg.substring(addLength).toLowerCase();
                    if (!Config.getInstance().autoInvitedPlayers.contains(player))
                    {
                        Config.getInstance().autoInvitedPlayers.add(player);
                    }
                    ModUtil.sendMessage(MCColor.AQUA + "Auto Inviting: \"" + player + "\"");
                    Utils.reporting(Config::save);
                }
                else
                {
                    ModUtil.sendMessage(MCColor.RED + "Invalid Username: \""
                            + msg.substring(addLength).toLowerCase() + "\"");
                }
            }
            else if (msg.startsWith("/ap remove "))
            {
                if (msg.matches("/ap remove [a-zA-Z0-9_]+"))
                {
                    String player = msg.substring(removeLength).toLowerCase();
                    if (Config.getInstance().autoInvitedPlayers.contains(player))
                    {
                        Config.getInstance().autoInvitedPlayers.remove(player);
                    }
                    ModUtil.sendMessage(
                            MCColor.RED + "No Longer Auto Inviting: \"" + player + "\"");
                    Utils.reporting(Config::save);
                }
                else
                {
                    ModUtil.sendMessage(MCColor.RED + "Invalid Username: \""
                            + msg.substring(removeLength).toLowerCase() + "\"");
                }
            }
            else if (msg.startsWith("/ap list"))
            {
                ModUtil.sendMessage(MCColor.AQUA + "Auto Inviting:");
                if (Config.getInstance().autoInvitedPlayers.size() == 0)
                {
                    ModUtil.sendMessage(MCColor.AQUA + "<Nobody>");
                }
                else
                {
                    for (String player : Config.getInstance().autoInvitedPlayers)
                    {
                        ModUtil.sendMessage(MCColor.AQUA + player);
                    }
                }
            }
            else
            {
                ModUtil.sendMessage(MCColor.RED + "Usage: /ap [add | remove | list] <player>");
            }

            return true;
        }
        else if (msg.equals("/ap"))
        {
            Config.getInstance().autoInviteEnabled = !Config.getInstance().autoInviteEnabled;
            if (Config.getInstance().autoInviteEnabled)
            {
                ModUtil.sendMessage(MCColor.AQUA + "Auto Invite Enabled!");
            }
            else
            {
                ModUtil.sendMessage(MCColor.RED + "Auto Invite Disabled!");
            }
            Utils.reporting(Config::save);

            return true;
        }

        return false;
    }

}
