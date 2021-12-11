package com.coolspy3.cspartymanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coolspy3.csmodloader.network.SubscribeToPacketStream;
import com.coolspy3.csmodloader.util.Utils;
import com.coolspy3.cspackets.datatypes.MCColor;
import com.coolspy3.cspackets.packets.ClientChatSendPacket;
import com.coolspy3.util.ModUtil;

public class PartyPresetSetCommand
{

    public static final String regex = "/pmset ([0-9][0-9]?[0-9]?) ([a-zA-Z0-9_]+)( .*)?";
    public static final Pattern pattern = Pattern.compile(regex);

    @SubscribeToPacketStream
    public boolean register(ClientChatSendPacket event)
    {
        String msg = event.msg;
        if (msg.startsWith("/pmset ") || msg.equals("/pmset"))
        {
            Matcher matcher = pattern.matcher(msg);
            if (matcher.matches())
            {
                Integer idx = Integer.parseInt(matcher.group(1));
                String player = matcher.group(2);
                Config.getInstance().presets.put(idx, player);
                ModUtil.sendMessage(MCColor.AQUA + "Preset Set!");
                Utils.reporting(Config::save);
            }
            else
                ModUtil.sendMessage(MCColor.RED + "Usage: /pmset <preset> <player>");

            return true;
        }

        return false;
    }

}
