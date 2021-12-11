package com.coolspy3.cspartymanager;

import com.coolspy3.csmodloader.network.SubscribeToPacketStream;
import com.coolspy3.csmodloader.util.Utils;
import com.coolspy3.cspackets.datatypes.MCColor;
import com.coolspy3.cspackets.packets.ClientChatSendPacket;
import com.coolspy3.util.ModUtil;

public class KickOfflinePartyMembersCommand
{

    @SubscribeToPacketStream
    public boolean register(ClientChatSendPacket event)
    {
        if (event.msg.matches("/kopm( .*)?"))
        {
            Config.getInstance().kickOfflinePartyMembers =
                    !Config.getInstance().kickOfflinePartyMembers;
            if (Config.getInstance().kickOfflinePartyMembers)
            {
                ModUtil.sendMessage(MCColor.AQUA + "Kick Offline Party Members Enabled!");
            }
            else
            {
                ModUtil.sendMessage(MCColor.RED + "Kick Offline Party Members Disabled!");
            }
            Utils.reporting(Config::save);

            return true;
        }

        return false;
    }

}
