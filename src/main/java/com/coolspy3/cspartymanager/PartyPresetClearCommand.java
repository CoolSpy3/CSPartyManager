package com.coolspy3.cspartymanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coolspy3.csmodloader.network.SubscribeToPacketStream;
import com.coolspy3.csmodloader.util.Utils;
import com.coolspy3.cspackets.datatypes.MCColor;
import com.coolspy3.cspackets.packets.ClientChatSendPacket;
import com.coolspy3.util.ModUtil;

public class PartyPresetClearCommand
{

    public static final String regex = "/pmclear ([0-9][0-9]?[0-9]?)( .*)?";
    public static final Pattern pattern = Pattern.compile(regex);

    @SubscribeToPacketStream
    public boolean register(ClientChatSendPacket event)
    {
        String msg = event.msg;

        if (msg.startsWith("/pmclear ") || msg.equals("/pmclear"))
        {
            Matcher matcher = pattern.matcher(msg);

            if (matcher.matches())
            {
                Integer idx = Integer.parseInt(matcher.group(1));
                Config.getInstance().presets.remove(idx);
                ModUtil.sendMessage(MCColor.RED + "Preset Cleared!");
                Utils.reporting(Config::save);
            }
            else
            {
                ModUtil.sendMessage(MCColor.RED + "Usage: /pmclear <preset>");
            }

            return true;
        }

        return false;
    }

}
