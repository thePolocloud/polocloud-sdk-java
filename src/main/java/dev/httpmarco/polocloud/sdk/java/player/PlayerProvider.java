package dev.httpmarco.polocloud.sdk.java.player;

import com.google.protobuf.Empty;
import dev.httpmarco.polocloud.sdk.java.Polocloud;
import dev.httpmarco.polocloud.sdk.java.utils.FutureConverter;
import dev.httpmarco.polocloud.shared.player.PolocloudPlayer;
import dev.httpmarco.polocloud.shared.player.SharedPlayerProvider;
import dev.httpmarco.polocloud.shared.service.Service;
import dev.httpmarco.polocloud.v1.player.*;
import io.grpc.ManagedChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerProvider implements SharedPlayerProvider<PolocloudPlayer> {

    private final PlayerControllerGrpc.PlayerControllerBlockingStub blockingStub;
    private final PlayerControllerGrpc.PlayerControllerFutureStub futureStub;

    public PlayerProvider(ManagedChannel channel) {
        this.blockingStub = PlayerControllerGrpc.newBlockingStub(channel);
        this.futureStub = PlayerControllerGrpc.newFutureStub(channel);
    }

    @Override
    public @NotNull List<PolocloudPlayer> findAll() {
        return this.blockingStub.findAll(Empty.getDefaultInstance()).getPlayersList().stream().map(PolocloudPlayer.Companion::from).toList();
    }

    @Override
    public @NotNull CompletableFuture<List<PolocloudPlayer>> findAllAsync() {
        return FutureConverter.completableFromGuava(this.futureStub.findAll(Empty.getDefaultInstance()), findAllPlayerResponse -> findAllPlayerResponse.getPlayersList().stream().map(PolocloudPlayer.Companion::from).toList());
    }

    @Override
    @Nullable
    public PolocloudPlayer findByName(@NotNull String name) {
        return this.blockingStub.findByName(PlayerFindByNameRequest.newBuilder().setName(name).build()).getPlayersList().stream().map(PolocloudPlayer.Companion::from).findFirst().orElse(null);
    }

    @Override
    @NotNull
    public CompletableFuture<PolocloudPlayer> findByNameAsync(@NotNull String name) {
        return FutureConverter.completableFromGuava(this.futureStub.findByName(PlayerFindByNameRequest.newBuilder().setName(name).build()),
                findGroupResponse -> findGroupResponse.getPlayersList().stream().map(PolocloudPlayer.Companion::from).findFirst().orElse(null));
    }

    @Override
    @NotNull
    public List<PolocloudPlayer> findByService(@NotNull String serviceName) {
        return this.blockingStub.findByService(PlayerFindByServiceRequest.newBuilder().setCurrentServiceName(serviceName).build()).getPlayersList().stream().map(PolocloudPlayer.Companion::from).toList();
    }

    @Override
    @NotNull
    public CompletableFuture<List<PolocloudPlayer>> findByServiceAsync(@NotNull Service service) {
        return FutureConverter.completableFromGuava(this.futureStub.findByService(PlayerFindByServiceRequest.newBuilder().setCurrentServiceName(service.name()).build()),
                findByServiceRequest -> findByServiceRequest.getPlayersList().stream().map(PolocloudPlayer.Companion::from).toList());
    }

    @Override
    public int playerCount() {
        return this.blockingStub.playerCount(Empty.getDefaultInstance()).getCount();
    }


    @Override
    public @NotNull PlayerActorResponse messagePlayer(@NotNull UUID uniqueId, @NotNull String message) {
        return this.blockingStub.messagePlayer(PlayerMessageActorRequest.newBuilder().setUniqueId(uniqueId.toString()).setMessage(message).build());
    }

    @Override
    public @NotNull PlayerActorResponse kickPlayer(@NotNull UUID uniqueId, @NotNull String message) {
        return this.blockingStub.kickPlayer(PlayerKickActorRequest.newBuilder().setUniqueId(uniqueId.toString()).setReason(message).build());
    }

    @Override
    public @NotNull PlayerActorResponse connectPlayerToService(@NotNull UUID uniqueId, @NotNull String serviceName) {
        return this.blockingStub.connectPlayer(PlayerConnectActorRequest.newBuilder().setUniqueId(uniqueId.toString()).setTargetServiceName(serviceName).build());
    }

    @Override
    public @Nullable PolocloudPlayer findByUniqueId(@NotNull UUID uuid) {
        return this.blockingStub.findByUniqueID(PlayerFindByUniqueIdRequest.newBuilder().setUniqueId(uuid.toString()).build()).getPlayersList().stream().map(PolocloudPlayer.Companion::from).findFirst().orElse(null);
    }

    @Override
    public @NotNull CompletableFuture<PolocloudPlayer> findByUniqueIdAsync(@NotNull UUID uuid) {
        return FutureConverter.completableFromGuava(this.futureStub.findByUniqueID(PlayerFindByUniqueIdRequest.newBuilder().setUniqueId(uuid.toString()).build()),
                findGroupResponse -> findGroupResponse.getPlayersList().stream().map(PolocloudPlayer.Companion::from).findFirst().orElse(null));
    }

    public void verifyActorStreaming() {
        this.blockingStub.actorStreaming(PlayerActorIdentifier.newBuilder().setServiceName(Polocloud.instance().selfServiceName()).build());
    }
}
