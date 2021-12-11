package com.coolspy3.cspartymanager;

import com.coolspy3.csmodloader.network.SubscribeToPacketStream;
import com.coolspy3.cspackets.datatypes.MCColor;
import com.coolspy3.cspackets.packets.ClientChatSendPacket;
import com.coolspy3.util.ModUtil;

public class HelpCommand
{

    public static final String justABunchOfDashes = "-----------------------------";

    @SubscribeToPacketStream
    public boolean register(ClientChatSendPacket event)
    {
        if (event.msg.matches("/pmhelp( .*)?"))
        {
            ModUtil.sendMessage(MCColor.BLUE + justABunchOfDashes);
            ModUtil.sendMessage(
                    MCColor.YELLOW + "/ap" + MCColor.AQUA + " - Toggle Party Auto Invite");
            ModUtil.sendMessage(MCColor.YELLOW + "/ap add [username]" + MCColor.AQUA
                    + " - Auto Invite the Specified User When They Are Online (This only fully works with friends)");
            ModUtil.sendMessage(MCColor.YELLOW + "/ap remove [username]" + MCColor.AQUA
                    + " - Stop Auto Inviting The Specified User");
            ModUtil.sendMessage(MCColor.YELLOW + "/ap list" + MCColor.AQUA
                    + " - Lists The Players Who Will Be Auto Invited");
            ModUtil.sendMessage(
                    MCColor.YELLOW + "/apa" + MCColor.AQUA + " - Toggle Party Request Auto Accept");
            ModUtil.sendMessage(MCColor.YELLOW + "/apa add [username]" + MCColor.AQUA
                    + " - Auto Accept Party Requests From The Specified User");
            ModUtil.sendMessage(MCColor.YELLOW + "/apa remove [username]" + MCColor.AQUA
                    + " - Stop Auto Accepting Party Requests From The Specified User");
            ModUtil.sendMessage(MCColor.YELLOW + "/apa list" + MCColor.AQUA
                    + " - Lists The Players Whose Party Requests Will Be Auto Accepted");
            ModUtil.sendMessage(MCColor.YELLOW + "/kopm" + MCColor.AQUA
                    + " - Toggle Auto Kick Of Offline Party Members");
            ModUtil.sendMessage(MCColor.YELLOW + "/p[num]" + MCColor.AQUA
                    + " - Party The Specified Preset Player (0-999)");
            ModUtil.sendMessage(MCColor.YELLOW + "/pmclear [num]" + MCColor.AQUA
                    + " - Clear The Specified Player Preset (0-999)");
            ModUtil.sendMessage(
                    MCColor.YELLOW + "/pmhelp" + MCColor.AQUA + " - Show This Help Menu");
            ModUtil.sendMessage(
                    MCColor.YELLOW + "/pmlist" + MCColor.AQUA + " - List All Party Presets");
            ModUtil.sendMessage(MCColor.YELLOW + "/pmlist [num]" + MCColor.AQUA
                    + " - List The Player Associated With Party Preset #[num] (0-999)");
            ModUtil.sendMessage(MCColor.YELLOW + "/pmset [num] [username]" + MCColor.AQUA
                    + " - Sets The Player Associated With Party Preset #[num] (0-999)");
            ModUtil.sendMessage(MCColor.BLUE + justABunchOfDashes);

            return true;
        }

        return false;
    }

}
