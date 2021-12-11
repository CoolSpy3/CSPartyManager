package com.coolspy3.cspartymanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coolspy3.csmodloader.network.PacketHandler;
import com.coolspy3.csmodloader.network.SubscribeToPacketStream;
import com.coolspy3.cspackets.datatypes.MCColor;
import com.coolspy3.cspackets.packets.ClientChatSendPacket;
import com.coolspy3.util.ModUtil;

public class PartyPresetCommand
{

    public static final String regex = "/p([0-9][0-9]?[0-9]?)( .*)?";
    public static final Pattern pattern = Pattern.compile(regex);

    @SubscribeToPacketStream
    public boolean register(ClientChatSendPacket event)
    {
        String msg = event.msg;
        Matcher matcher = pattern.matcher(msg);

        if (matcher.matches())
        {
            Integer idx = Integer.parseInt(matcher.group(1));
            if (Config.getInstance().presets.containsKey(idx)) PacketHandler.getLocal().sendPacket(
                    new ClientChatSendPacket("/p " + Config.getInstance().presets.get(idx)));

            else
                ModUtil.sendMessage(MCColor.RED + "Preset: " + idx + " is Undefined!");

            return true;
        }

        return false;
    }

}
