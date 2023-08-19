package dev.buildtool.kurretsfabric.client.screens;

import dev.buildtool.kurretsfabric.Drone;
import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.Turret;
import dev.buildtool.satako.IntegerColor;
import dev.buildtool.satako.UniqueList;
import dev.buildtool.satako.gui.*;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TurretOptionsScreen extends BetterScreen {
    private final Turret turret;
    private HashMap<EntityType<?>, Boolean> tempStatusMap;
    private List<EntityType<?>> targets;
    private static final Text CHOICE_HINT = Text.translatable("k_turrets.choose.tooltip");
    private List<SwitchButton> targetButtons;
    private TextField addEntityField;
    private List<String> exceptions;
    private HashMap<String, Boolean> tempExceptionStatus;
    private List<Label> suggestions;
    private BetterButton addTarget, dismantle, clearTargets, resetList, mobilitySwitch, protectionFromPlayers, claimTurret,
            followSwitch;
    private boolean renderLabels = true;

    public TurretOptionsScreen(Turret turret) {
        super(Text.translatable("k_turrets.targets"));
        this.turret = turret;
        targets = new UniqueList<>(Turret.decodeTargets(turret.getTargets()));
    }

    @Override
    protected void init() {
        super.init();
        tempStatusMap = new HashMap<>(40);
        targets.forEach(entityType -> tempStatusMap.put(entityType, true));
        exceptions = turret.getExceptions();
        tempExceptionStatus = new HashMap<>(1);
        exceptions.forEach(s -> tempExceptionStatus.put(s, true));
        suggestions = new ArrayList<>(12);
        addEntityField = addDrawableChild(new TextField(centerX, 3, 180));
        addTarget = addDrawableChild(new BetterButton(centerX, 20, Text.translatable("k_turrets.add.entity.type"), button -> {
            String s = addEntityField.getText();
            if (s.startsWith("!")) {
                if (s.length() > 1) {
                    String playerName = s.substring(1);
                    if (exceptions.contains(playerName))
                        client.player.sendMessage(Text.translatable("k_turrets.player.is.already.in.exceptions", playerName), true);
                    else {
                        turret.addPlayerToExceptions(playerName);
                        tempExceptionStatus.put(playerName, true);
                        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                        packetByteBuf.writeInt(turret.getId());
                        packetByteBuf.writeString(playerName);
                        ClientPlayNetworking.send(KTurrets.addPlayerException, packetByteBuf);
                        addEntityField.setText("");
                        clearAndInit();
                    }
                }
            } else {
                EntityType<?> type = Registry.ENTITY_TYPE.get(new Identifier(s));
                if (s.length() > 2) {
                    if (type == EntityType.PIG && !s.equals("minecraft:pig") && !s.equals("pig")) {
                        client.player.sendMessage(Text.translatable("k_turrets.incorrect.entry"), true);
                    } else {
                        targets.add(type);
                        tempStatusMap.put(type, true);
                        if (s.contains(":"))
                            addEntityField.setText(s.substring(0, s.indexOf(':')));
                        clearAndInit();
                    }
                }
            }
        }));

        dismantle = addDrawableChild(new BetterButton(centerX, 40, Text.translatable("k_turrets.dismantle"), button -> {
            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
            packetByteBuf.writeInt(turret.getId());
            ClientPlayNetworking.send(KTurrets.dismantle, packetByteBuf);
            client.player.closeScreen();
        }));
        clearTargets = addDrawableChild(new BetterButton(centerX, 60, Text.translatable("k_turrets.clear.list"), button -> {
            targets.clear();
            tempStatusMap.clear();
            targetButtons.forEach(switchButton -> {
                drawables.remove(switchButton);
                children.remove(switchButton);
                selectables.remove(switchButton);
            });
        }));
        resetList = addDrawableChild(new BetterButton(clearTargets.x + clearTargets.getElementWidth(), 60, Text.translatable("k_turrets.reset.list"), button -> {
            targets = Registry.ENTITY_TYPE.stream().filter(entityType1 -> !entityType1.getSpawnGroup().isPeaceful()).collect(Collectors.toList());
            targets.forEach(entityType -> tempStatusMap.put(entityType, true));
            clearAndInit();
        }));
        mobilitySwitch = addDrawableChild(new SwitchButton(centerX, 80, Text.translatable("k_turrets.mobile"), Text.translatable("k_turrets.immobile"), turret.isMobile(), button -> {
            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
            packetByteBuf.writeInt(turret.getId());
            packetByteBuf.writeBoolean(!turret.isMobile());
            ClientPlayNetworking.send(KTurrets.toggleMobility, packetByteBuf);
            turret.setMobile(!turret.isMobile());
        }));
        protectionFromPlayers = addDrawableChild(new SwitchButton(centerX, 100, Text.translatable("k_turrets.protect.from.players"), Text.translatable("k_turrets.not.protect.from.players"), turret.isProtectingFromPlayers(), button -> {
            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
            packetByteBuf.writeInt(turret.getId());
            packetByteBuf.writeBoolean(!turret.isProtectingFromPlayers());
            ClientPlayNetworking.send(KTurrets.togglePlayerProtection, packetByteBuf);
            turret.setProtectingFromPlayers(!turret.isProtectingFromPlayers());
        }));
        if (turret.getOwner().isEmpty()) {
            claimTurret = addDrawableChild(new BetterButton(centerX, 120, Text.translatable(turret instanceof Drone ? "k_turrets.claim.drone" : "k_turrets.claim.turret"), button -> {
                PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                packetByteBuf.writeInt(turret.getId());
                packetByteBuf.writeUuid(client.player.getUuid());
                ClientPlayNetworking.send(KTurrets.claim, packetByteBuf);
                turret.setOwner(client.player.getUuid());
                clearAndInit();
            }));
        } else if (turret instanceof Drone drone) {
            DropDownButton dropDownButton = new DropDownButton(centerX, 120, this, Text.literal("K-Turrets"));
            LinkedHashMap<Text, ButtonWidget.PressAction> linkedHashMap = new LinkedHashMap<>(3);
            RadioButton follow = new RadioButton(centerX, 140, Text.translatable("k_turrets.following.owner"));
            linkedHashMap.put(follow.getMessage(), p_93751_ -> {
                drone.setFollowingOwner(true);
                drone.setGuardingArea(false);
                PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
                byteBuf.writeInt(drone.getId());
                byteBuf.writeBoolean(true);
                ClientPlayNetworking.send(KTurrets.toggleFollow, byteBuf);
                PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                packetByteBuf.writeInt(drone.getId());
                packetByteBuf.writeBoolean(false);
                ClientPlayNetworking.send(KTurrets.setGuardArea, packetByteBuf);
                dropDownButton.setMessage(p_93751_.getMessage());
                dropDownButton.onPress();
            });
            RadioButton stay = new RadioButton(centerX, 160, Text.translatable("k_turrets.staying"));
            linkedHashMap.put(stay.getMessage(), p_93751_ -> {
                drone.setFollowingOwner(false);
                drone.setGuardingArea(false);
                PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
                byteBuf.writeInt(drone.getId());
                byteBuf.writeBoolean(false);
                ClientPlayNetworking.send(KTurrets.toggleFollow, byteBuf);
                PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                packetByteBuf.writeInt(drone.getId());
                packetByteBuf.writeBoolean(false);
                ClientPlayNetworking.send(KTurrets.setGuardArea, packetByteBuf);
                dropDownButton.setMessage(p_93751_.getMessage());
                dropDownButton.onPress();
            });
            RadioButton guard = new RadioButton(centerX, 180, Text.translatable("k_turrets.guard.area"));
            linkedHashMap.put(guard.getMessage(), p_93751_ -> {
                drone.setFollowingOwner(false);
                drone.setGuardingArea(true);
                PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
                byteBuf.writeInt(drone.getId());
                byteBuf.writeBoolean(false);
                ClientPlayNetworking.send(KTurrets.toggleFollow, byteBuf);
                PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                packetByteBuf.writeInt(drone.getId());
                packetByteBuf.writeBoolean(true);
                ClientPlayNetworking.send(KTurrets.setGuardArea, packetByteBuf);
                dropDownButton.setMessage(p_93751_.getMessage());
                dropDownButton.onPress();
            });
            dropDownButton.setChoices(linkedHashMap, drone.isGuardingArea() ? 2 : drone.isFollowingOwner() ? 0 : 1);
            addSelectableChild(dropDownButton);
        }
        List<ClickableWidget> clickableWidgets = new ArrayList<>();
        List<SwitchButton> exceptionButtons = new ArrayList<>(19);
        if (!exceptions.isEmpty()) {
            Label label = new Label(3, 3, Text.translatable("k_turrets.exceptions").append(":"));
            addDrawableChild(label);
            clickableWidgets.add(label);
            for (int i = 0; i < exceptions.size(); i++) {
                String next = exceptions.get(i);
                SwitchButton switchButton = new SwitchButton(3, 20 * i + label.getY() + label.getHeight(), Text.literal(next), Text.literal(Formatting.STRIKETHROUGH + next), true, p_93751_ -> {
                    if (p_93751_ instanceof SwitchButton switchButton1) {
                        tempExceptionStatus.put(next, switchButton1.state);
                    }
                });
                addDrawableChild(switchButton);
                exceptionButtons.add(switchButton);
                clickableWidgets.add(switchButton);

            }
        }

        Label label = addDrawableChild(new Label(3, !exceptionButtons.isEmpty() ? exceptionButtons.get(exceptionButtons.size() - 1).getY() + exceptionButtons.get(exceptionButtons.size() - 1).getHeight() + 20 : 3, Text.translatable("k_turrets.targets")));
        clickableWidgets.add(label);
        targetButtons = new ArrayList<>(targets.size());
        targets.sort(Comparator.comparing(entityType -> Registry.ENTITY_TYPE.getId(entityType).toString()));
        for (int i = 0; i < targets.size(); i++) {
            EntityType<?> entityType = targets.get(i);
            SwitchButton switchButton = new SwitchButton(3, 20 * i + label.getY() + label.getHeight(), Text.literal(Registry.ENTITY_TYPE.getId(entityType).toString()), Text.literal(Formatting.STRIKETHROUGH + Registry.ENTITY_TYPE.getId(entityType).toString()), true, p_onPress_1_ -> {
                if (p_onPress_1_ instanceof SwitchButton switchButton1) {
                    tempStatusMap.put(entityType, switchButton1.state);
                }
            });
            addDrawableChild(switchButton);
            targetButtons.add(switchButton);
            clickableWidgets.add(switchButton);
        }
        ScrollArea scrollArea = new ScrollArea(3, 3, centerX - 15, height, Text.literal(""), new IntegerColor(0x228FDBF0), clickableWidgets);
        addDrawableChild(scrollArea);
    }

    @Override
    public void close() {
        super.close();
        AtomicInteger removed = new AtomicInteger();
        tempStatusMap.forEach((entityType, aBoolean) -> {
            if (aBoolean)
                targets.add(entityType);
            else {
                targets.remove(entityType);
                removed.getAndIncrement();
            }
        });
        NbtCompound compoundNBT = Turret.encodeTargets(targets);
        turret.setTargets(compoundNBT);
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeInt(turret.getId());
        packetByteBuf.writeNbt(compoundNBT);
        ClientPlayNetworking.send(KTurrets.targets, packetByteBuf);
        if (removed.get() > 0)
            client.player.sendMessage(Text.translatable("k_turrets.removed", removed.get()), false);
        tempExceptionStatus.forEach((s, aBoolean) -> {
            if (aBoolean) {
                if (!exceptions.contains(s)) {
                    turret.addPlayerToExceptions(s);
                    PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
                    byteBuf.writeInt(turret.getId());
                    byteBuf.writeString(s);
                    ClientPlayNetworking.send(KTurrets.addPlayerException, byteBuf);
                }
            } else {
                turret.removePlayerFromExceptions(s);
                PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
                byteBuf.writeInt(turret.getId());
                byteBuf.writeString(s);
                ClientPlayNetworking.send(KTurrets.removePlayerException, byteBuf);
            }
        });
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (renderLabels) {
            renderTooltip(matrices, Collections.singletonList(Text.translatable("k_turrets.integrity").append(": " + (int) turret.getHealth() + "/" + turret.getMaxHealth())), centerX, centerY + 40);
            List<Text> lines = split(CHOICE_HINT, width / 2 - 10);
            for (int i = 0; i < lines.size(); i++) {
                renderTooltip(matrices, lines.get(i), centerX, centerY + 80 + i * 20);
            }
            if (turret.getAutomaticTeam().isEmpty()) {
                renderTooltip(matrices, Collections.singletonList(Text.translatable("k_turrets.no.team")), centerX, centerY + 60);
            } else {
                renderTooltip(matrices, Collections.singletonList(Text.translatable("k_turrets.team").append(": " + turret.getAutomaticTeam())), centerX, centerY + 60);
            }
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (addEntityField.isFocused()) {
            suggestions.forEach(this::remove);
            suggestions.clear();
            String text = addEntityField.getText();
            if (text.length() > 0) {
                List<Identifier> entityTypes;
                if (text.contains(":"))
                    entityTypes = new ArrayList<>(Registry.ENTITY_TYPE.getIds().stream().filter(resourceLocation -> resourceLocation.toString().contains(text)).toList());
                else
                    entityTypes = new ArrayList<>(Registry.ENTITY_TYPE.getIds().stream().filter(resourceLocation -> resourceLocation.getNamespace().contains(text)).toList());
                int yOffset = 20;
                entityTypes.removeAll(targets.stream().map(Registry.ENTITY_TYPE::getId).toList());
                for (Identifier entityType : entityTypes.subList(0, Math.min(entityTypes.size(), 14))) {
                    Label hint = new Label(addEntityField.x, addEntityField.y + yOffset, Text.literal(Formatting.YELLOW + entityType.toString()), this, p_93751_ -> {
                        addEntityField.setText(p_93751_.getMessage().getString().substring(2));
                        suggestions.forEach(this::remove);
                        suggestions.clear();
                        showButtonsAndHints();
                    });
                    addDrawableChild(hint);
                    suggestions.add(hint);
                    yOffset += 14;
                }
                if (!entityTypes.isEmpty()) {
                    this.addTarget.setHidden(true);
                    if (claimTurret != null)
                        this.claimTurret.setHidden(true);
                    this.clearTargets.setHidden(true);
                    this.dismantle.setHidden(true);
                    if (followSwitch != null)
                        this.followSwitch.setHidden(true);
                    this.mobilitySwitch.setHidden(true);
                    this.protectionFromPlayers.setHidden(true);
                    this.resetList.setHidden(true);
                    renderLabels = false;
                } else {
                    showButtonsAndHints();
                }
            } else {
                showButtonsAndHints();
            }
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void showButtonsAndHints() {
        this.addTarget.setHidden(false);
        if (claimTurret != null)
            this.claimTurret.setHidden(false);
        this.clearTargets.setHidden(false);
        this.dismantle.setHidden(false);
        if (followSwitch != null)
            this.followSwitch.setHidden(false);
        this.mobilitySwitch.setHidden(false);
        this.protectionFromPlayers.setHidden(false);
        this.resetList.setHidden(false);
        renderLabels = true;
    }


    private List<Text> split(Text text, int maxWidth) {
        TextRenderer textRenderer1 = MinecraftClient.getInstance().textRenderer;
        int stringWidth = textRenderer1.getWidth(text);
        if (stringWidth > maxWidth) {
            String wrapped = WordUtils.wrap(text.getString(), maxWidth / 6, "\n", false);
            String[] parts = wrapped.split("\n");
            return Arrays.stream(parts).map(Text::literal).collect(Collectors.toList());
        }
        return List.of(text);
    }
}
