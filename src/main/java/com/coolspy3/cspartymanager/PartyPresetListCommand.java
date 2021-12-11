package com.coolspy3.cspartymanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coolspy3.csmodloader.network.SubscribeToPacketStream;
import com.coolspy3.cspackets.datatypes.MCColor;
import com.coolspy3.cspackets.packets.ClientChatSendPacket;
import com.coolspy3.util.ModUtil;

public class PartyPresetListCommand
{

    public static final String regex = "/pmlist ([0-9][0-9]?[0-9]?)( .*)?";
    public static final Pattern pattern = Pattern.compile(regex);

    @SubscribeToPacketStream
    public boolean register(ClientChatSendPacket event)
    {
        String msg = event.msg;

        if (msg.startsWith("/pmlist ") || msg.equals("/pmlist"))
        {
            Matcher matcher = pattern.matcher(msg);
            if (matcher.matches())
            {
                Integer idx = Integer.parseInt(matcher.group(1));
                if (Config.getInstance().presets.containsKey(idx))
                {
                    ModUtil.sendMessage(MCColor.AQUA + "Preset: " + idx + " is set to player: \""
                            + Config.getInstance().presets.get(idx) + "\"");
                }
            }
            else
            {
                ModUtil.sendMessage(MCColor.AQUA + "Presets:");
                if (Config.getInstance().presets.isEmpty())
                    ModUtil.sendMessage(MCColor.AQUA + "<None>");

                else
                    for (int idx : Config.getInstance().presets.keySet())
                        ModUtil.sendMessage(MCColor.AQUA + "" + idx + ": "
                                + Config.getInstance().presets.get(idx));
            }

            return true;
        }

        return false;
    }

}
