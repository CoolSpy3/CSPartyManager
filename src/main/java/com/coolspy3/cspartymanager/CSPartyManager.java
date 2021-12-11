package com.coolspy3.cspartymanager;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.coolspy3.csmodloader.GameArgs;
import com.coolspy3.csmodloader.interfaces.ExceptionSupplier;
import com.coolspy3.csmodloader.mod.Entrypoint;
import com.coolspy3.csmodloader.mod.Mod;
import com.coolspy3.csmodloader.network.PacketHandler;
import com.coolspy3.csmodloader.network.SubscribeToPacketStream;
import com.coolspy3.csmodloader.util.Utils;
import com.coolspy3.cspackets.packets.ClientChatSendPacket;
import com.coolspy3.cspackets.packets.ServerChatSendPacket;
import com.coolspy3.hypixelapi.APIConfig;
import com.coolspy3.util.ModUtil;
import com.coolspy3.util.ServerJoinEvent;

import me.kbrewster.exceptions.APIException;
import me.kbrewster.mojangapi.MojangAPI;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.FriendsReply;
import net.hypixel.api.reply.FriendsReply.FriendShip;

@Mod(id = "cspartymanager", name = "CSPartyManager",
        description = "Adds commands for management of parties on Hypixel", version = "2.0.0",
        dependencies = {"csmodloader:[1,2)", "cspackets:[1,2)", "csutils:[1,2)",
                "cshypixelapi:[2,3)"})
public class CSPartyManager implements Entrypoint
{

    public static final String inviteRegex =
            "^-----------------------------\\n(\\[[a-zA-Z0-9_\\+]+\\] )?([a-zA-Z0-9_]+) has invited you to join their party!\\nYou have 60 seconds to accept\\. Click here to join!\\n-----------------------------$";
    public static final Pattern invitePattern = Pattern.compile(inviteRegex);
    public static final String joinRegex = "Friend > ([a-zA-Z0-9_]+) joined\\.";
    public static final Pattern joinPattern = Pattern.compile(joinRegex);
    public static final String leaveRegex =
            "^(\\[[a-zA-Z0-9_\\+]+\\] )?([a-zA-Z0-9_]+) has disconnected, they have 5 minutes to rejoin before they are removed from the party\\.$";
    public static final Pattern leavePattern = Pattern.compile(leaveRegex);

    @Override
    public void init(PacketHandler handler)
    {
        Utils.reporting(Config::load);
        handler.register(this);
        handler.register(new AutoAcceptCommand());
        handler.register(new AutoPartyCommand());
        handler.register(new HelpCommand());
        handler.register(new KickOfflinePartyMembersCommand());
        handler.register(new PartyPresetClearCommand());
        handler.register(new PartyPresetCommand());
        handler.register(new PartyPresetListCommand());
        handler.register(new PartyPresetSetCommand());
    }

    @SubscribeToPacketStream
    public void onChatMessageReceived(ServerChatSendPacket event)
    {
        if (Config.getInstance().autoAcceptEnabled)
        {
            String msg = event.msg;
            Matcher inviteMatcher = invitePattern.matcher(msg);
            if (inviteMatcher.matches())
            {
                String player = inviteMatcher.group(2).toLowerCase();
                if (Config.getInstance().autoAcceptedPlayers.contains(player))
                {
                    PacketHandler.getLocal()
                            .sendPacket(new ClientChatSendPacket("/p accept " + player));
                }
            }
        }
        if (Config.getInstance().autoInviteEnabled)
        {
            String msg = event.msg;
            Matcher joinMatcher = joinPattern.matcher(msg);
            if (joinMatcher.matches())
            {
                String player = joinMatcher.group(1).toLowerCase();
                if (Config.getInstance().autoInvitedPlayers.contains(player))
                {
                    ModUtil.executeAsync(() -> {
                        try
                        {
                            Thread.sleep(3000);
                        }
                        catch (InterruptedException e)
                        {}
                        PacketHandler.getLocal()
                                .sendPacket(new ClientChatSendPacket("/p " + player));
                    });
                }
            }
        }
        if (Config.getInstance().kickOfflinePartyMembers)
        {
            String msg = event.msg;
            Matcher leaveMatcher = leavePattern.matcher(msg);
            if (leaveMatcher.matches())
            {
                String player = leaveMatcher.group(2).toLowerCase();
                PacketHandler.getLocal().sendPacket(new ClientChatSendPacket("/p kick " + player));
            }
        }
    }

    @SubscribeToPacketStream
    public void onServerJoined(ServerJoinEvent event)
    {
        if (Config.getInstance().autoInviteEnabled
                && Config.getInstance().autoInvitedPlayers.size() != 0)
        {
            ModUtil.executeAsync(() -> {
                HypixelAPI api =
                        Utils.reporting((ExceptionSupplier<HypixelAPI>) APIConfig::requireAPI);

                if (api == null) return;

                FriendsReply friendsReply = api.getFriends(GameArgs.get().uuid).join();
                for (UUID friend : getFriends(friendsReply.getFriendShips()))
                {
                    try
                    {
                        String username = MojangAPI.getName(friend);
                        if (Config.getInstance().autoInvitedPlayers.contains(username.toLowerCase())
                                && api.getStatus(friend).join().getSession().isOnline())
                        {
                            PacketHandler.getLocal()
                                    .sendPacket(new ClientChatSendPacket("/p " + username));
                        }
                    }
                    catch (APIException | IOException e)
                    {
                        e.printStackTrace(System.err);
                    }
                }
            });
        }
    }

    public static List<UUID> getFriends(List<FriendShip> friends)
    {
        return friends.stream()
                .map(friendship -> friendship.getUuidSender().equals(GameArgs.get().uuid)
                        ? friendship.getUuidReceiver()
                        : friendship.getUuidSender())
                .collect(Collectors.toList());
    }
}
