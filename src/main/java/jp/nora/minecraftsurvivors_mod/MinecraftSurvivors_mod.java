package jp.nora.minecraftsurvivors_mod;

import com.mojang.blaze3d.matrix.MatrixStack;
import jp.nora.minecraftsurvivors_mod.countdown.CountdownOverlay;
import jp.nora.minecraftsurvivors_mod.custom_sounds.CustomMusicTickableSound;
import jp.nora.minecraftsurvivors_mod.custom_sounds.MSModSounds;
import jp.nora.minecraftsurvivors_mod.custom_sounds.NetworkHandler;
import jp.nora.minecraftsurvivors_mod.entity.render.RedZombieEntity;
import jp.nora.minecraftsurvivors_mod.ranking_board.NameInputScreen;
import jp.nora.minecraftsurvivors_mod.ranking_board.OpenRankingScreenPacket;
import jp.nora.minecraftsurvivors_mod.regi.ClientEventBusSubscriber;
import jp.nora.minecraftsurvivors_mod.regi.CommonEventBusSubscriber;
import jp.nora.minecraftsurvivors_mod.regi.MobEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod("minecraftsurvivors_mod")
public class MinecraftSurvivors_mod {
    // 以下の変数を追加
    private BlockPos originalPlayerPos;
    private ServerWorld originalPlayerWorld;
    private boolean gameActive = false;
    private long startTime = 0;
    private static int score = 0;
    private int numZombies = 2;
    private final int maxZombies = 200;
    private final int timeBetweenSpawns = 40; // 20 ticks = 1 second
    private int tickCounter = 0;
    private final int gameDuration = 100 * 20; // 20 ticks *
    private final Random random = new Random();
    private int countdown = 3;
    private boolean countdownInProgress = false;
    private long countdownStartTime;
    private boolean showMessage = false;
    private long showMessageStartTime;
    private int showMessageDuration = 2000; // 表示時間（ミリ秒）
    public static SoundEvent customBGM;
    public static final String MOD_ID = "minecraftsurvivors_mod";

    private CustomMusicTickableSound customMusic;

    //スポーン関連
    float chickenChance = 0.1f;
    float redZombieChance = 0.1f;
    float childZombieChance = 0.1f;
    long delay = 15 * 20; // 15秒後 (Minecraftでは、1秒 = 20 ticks)

    //ランキングボード
    private static int scoreboard;
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("minecraftsurvivors", "open_name_input_screen"))
            .clientAcceptedVersions(a -> true)
            .serverAcceptedVersions(a -> true)
            .networkProtocolVersion(() -> "1.0")
            .simpleChannel();

    public static int getScore() {
        return score;
    }

    public MinecraftSurvivors_mod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //Mod Event Busへの登録
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::setup);
        MobEntityTypes.ENTITY_TYPES.register(modEventBus);
        modEventBus.addListener(this::onClientSetup);

        // Minecraft Forge Event Busへの登録
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ClientEventBusSubscriber());
        MinecraftForge.EVENT_BUS.register(new CommonEventBusSubscriber());

        MSModSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());

        // その他の設定
        registerMessages();
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEventBusSubscriber::new); // renderRegister メソッドを実行するために、ClientEventBusSubscriber のインスタンスを作成します。
    }

    private void setup(final FMLCommonSetupEvent event) {
        NetworkHandler.init();
        MSModSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void registerMessages() {
        CHANNEL.messageBuilder(OpenRankingScreenPacket.class, 0)
                .encoder((pkt, buf) -> {
                })
                .decoder(buf -> new OpenRankingScreenPacket())
                .consumer((BiConsumer<OpenRankingScreenPacket, Supplier<NetworkEvent.Context>>) (pkt, ctx) -> ctx.get().enqueueWork(() -> openNameInputScreen()))
                .add();
    }

    @SubscribeEvent
    public static void onSoundEventRegistry(final RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();

        customBGM = new SoundEvent(new ResourceLocation("minecraftsurvivors_mod", "battle_bgm"));
        //pathはsoundsフォルダより後の部分を指定.ogg拡張子は不要
        registry.register(customBGM);
    }


    //値をリセットするメソッド
    public void reset() {
        gameActive = false;
        startTime = 0;
        numZombies = 2;
        tickCounter = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        PlayerEntity player = event.player;
        World world = player.getEntityWorld();

        if (!world.isRemote) {
            BlockPos platePos = new BlockPos(Math.floor(player.getPosX()), Math.floor(player.getPosY() - 1), Math.floor(player.getPosZ()));
            BlockState plateState = world.getBlockState(platePos);

            // プレートが押されたかどうか確認
            if (plateState.getBlock() == Blocks.STONE_PRESSURE_PLATE && plateState.get(PressurePlateBlock.POWERED)) {
                if (!gameActive && !countdownInProgress) {
                    originalPlayerPos = player.getPosition();
                    originalPlayerWorld = ((ServerPlayerEntity) player).getServerWorld();

                    // ゲームフィールドの生成
                    BlockPos gameFieldCenter = new BlockPos(5000, 100, 0);
                    createGameField(world, gameFieldCenter);

                    countdownInProgress = true;
                    countdownStartTime = System.currentTimeMillis();
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.YELLOW + "Countdown started..."), false);

                }
            }

            if (countdownInProgress) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - countdownStartTime >= 1000) {
                    countdownStartTime = currentTime;
                    countdown--;

                    if (countdown == 0) {
                        startGame(player);
                        countdownInProgress = false;
                        countdown = 4;
                    } else {
                        player.sendStatusMessage(new StringTextComponent(TextFormatting.YELLOW + "Game starts in: " + countdown), false);
                    }
                }
            }

            if (gameActive) {
                tickCounter++;

                //ゲームが終了する10秒前カウント
                if (tickCounter >= gameDuration - 210 && tickCounter <= gameDuration) {
                    if (tickCounter % 20 == 0) {
                        int countdown = (gameDuration - tickCounter) / 20;
                        CountdownOverlay.setCountdownTime(countdown);
                    }
                } else {
                    CountdownOverlay.setCountdownTime(0);
                }

                // ゲーム時間が経過したらゲーム終了
                if (tickCounter >= gameDuration) {
                    endGame(player);
                }

                // 時間経過でゾンビをスポーンさせる
                if (tickCounter % timeBetweenSpawns == 0 && numZombies < maxZombies) {
                    // 経過時間に応じて、一定間隔でゾンビの数を増やす
                    int additionalZombies = tickCounter / (20 * 30); // 30秒ごとにゾンビを1ずつ増やす
                    spawnEntities(world, player, 1 + additionalZombies, 5, 10, chickenChance, redZombieChance, childZombieChance, delay);  numZombies++;
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) throws InterruptedException {
        // プレイヤーが死亡した場合、ゲームを終了
        if (gameActive && event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            endGame(player);
        }

        // ゾンビを倒したらスコアを加算
        if (gameActive && event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            LivingEntity entity = (LivingEntity) event.getEntity();
            World world = player.getEntityWorld();

            // RedZombieを倒した場合、スコアを500加算
            if (entity instanceof RedZombieEntity) {
                MinecraftSurvivors_mod.score += 500;
                // カスタム効果音を再生する
                world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), MSModSounds.BIG_POINT.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            // 子ゾンビを倒した場合、スコアを300加算
            else if (entity instanceof ZombieEntity && ((ZombieEntity) entity).isChild()) {
                MinecraftSurvivors_mod.score += 300;
                // カスタム効果音を再生する
                world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), MSModSounds.POINT.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            // 通常のゾンビを倒した場合、スコアを100加算
            else if (entity instanceof ZombieEntity) {
                MinecraftSurvivors_mod.score += 100;
                // カスタム効果音を再生する
                world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), MSModSounds.POINT.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            // 鶏を倒した場合、体力ハートが3つ回復
            else if (entity instanceof ChickenEntity) {
                // 3つのハート分の体力を回復 (1ハート = 2体力ポイント)
                float healthToRecover = 3 * 2.0F;
                player.heal(healthToRecover);
                // カスタム効果音を再生する
                world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), MSModSounds.RECOVERY.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
            }

            // スコアボードを取得し、スコアを更新
            Scoreboard scoreboard = player.getEntityWorld().getScoreboard();
            String scoreName = "Survivors";
            ScoreObjective scoreObjective = scoreboard.getObjective(scoreName);

            if (scoreObjective != null) {
                Score score = scoreboard.getOrCreateScore(player.getDisplayName().getString(), scoreObjective);
                score.setScorePoints(MinecraftSurvivors_mod.score);
            }
        }
    }


    //画面中央の文字表示
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        // イベントタイプがTEXTであることを確認
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        // 1秒間だけメッセージを表示する
        long currentTime = System.currentTimeMillis();
        if (showMessage && currentTime - showMessageStartTime < 1000) {
            String text = "GameStart!";
            FontRenderer fontRenderer = minecraft.fontRenderer;
            MatrixStack matrixStack = new MatrixStack();
            int scale = 4;
            float alpha = 1.0f - (float) (currentTime - showMessageStartTime) / 1000.0f;
            int color = (int) (alpha * 255.0f) << 24 | 0xFFFFFF;
            int x = (minecraft.getMainWindow().getScaledWidth() - fontRenderer.getStringWidth(text) * scale) / 2;
            int y = (int) (minecraft.getMainWindow().getScaledHeight() * 0.3);
            matrixStack.push();
            matrixStack.translate(x, y, 0);
            matrixStack.scale(scale, scale, 1);
            fontRenderer.drawStringWithShadow(matrixStack, text, 0, 0, color);
            matrixStack.pop();
        }

        // ゲーム時間の表示
        if (gameActive) {
            int remainingTime = gameDuration - tickCounter;
            int remainingSeconds = remainingTime / 20;

            String text = remainingSeconds + "s";
            FontRenderer fontRenderer = minecraft.fontRenderer;

            // 画面の幅と高さを取得
            int screenWidth = minecraft.getMainWindow().getScaledWidth();
            int screenHeight = minecraft.getMainWindow().getScaledHeight();

            // 文字列の幅を取得
            int stringWidth = fontRenderer.getStringWidth(text);

            // 座標を画面中央の最上部に変更
            int x = (screenWidth - stringWidth) / 2;
            int y = 2;

            // MatrixStack オブジェクトを取得
            MatrixStack matrixStack = event.getMatrixStack();

            // MatrixStack オブジェクトを引数に追加
            fontRenderer.drawStringWithShadow(matrixStack, text, x, y, 0xFFFFFF);
        }

    }

    private void startGame(PlayerEntity player) {

        // ゲームスタート時に「スタート」表示
        player.sendStatusMessage(new StringTextComponent("MINECRAFT SURVIVORS START"), false);

        // ゲームの初期設定
        score = 0;
        gameActive = true;
        startTime = System.currentTimeMillis();
        showMessage = true;
        showMessageStartTime = System.currentTimeMillis();

        // ゲームフィールドへの移動と時間変更
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ServerWorld serverWorld = serverPlayer.getServerWorld();
            BlockPos gameFieldPos = new BlockPos(5000, 100, 0);
            serverPlayer.teleport(serverWorld, gameFieldPos.getX(), gameFieldPos.getY(), gameFieldPos.getZ(), player.rotationYaw, player.rotationPitch);
            serverWorld.setDayTime(13000);
        }

        // スコアボード作成と表示
        Scoreboard scoreboard = player.getEntityWorld().getScoreboard();
        String scoreName = "Survivors";
        ScoreObjective scoreObjective = scoreboard.getObjective(scoreName);
        if (scoreObjective == null) {
            scoreObjective = scoreboard.addObjective(scoreName, ScoreCriteria.DUMMY, new StringTextComponent("Score"), ScoreCriteria.RenderType.INTEGER);
        }
        scoreboard.setObjectiveInDisplaySlot(1, scoreObjective);

        // カスタムBGMの再生及び調整
        ResourceLocation soundLocation = new ResourceLocation("minecraftsurvivors_mod", "battle_bgm");
        SoundCategory soundCategory = SoundCategory.MUSIC;
        float volume = 0.3F;
        float pitch = 1.0F;
        customMusic = new CustomMusicTickableSound(soundLocation, soundCategory, volume, pitch);
        Minecraft.getInstance().getSoundHandler().play(customMusic);
    }

    // ゲームフィールドの生成メソッドを追加
    private void createGameField(World world, BlockPos center) {
        int fieldSize = 50;
        int lavaBorderWidth = 10; // 溶岩の幅を1ブロックに設定
        for (int x = -fieldSize / 2 - lavaBorderWidth; x <= fieldSize / 2 + lavaBorderWidth; x++) {
            for (int z = -fieldSize / 2 - lavaBorderWidth; z <= fieldSize / 2 + lavaBorderWidth; z++) {
                BlockPos blockPos = center.add(x, -1, z);

                // ゲームフィールドの内側にあるブロックをグロウストーンに設定
                if (x > -fieldSize / 2 && x < fieldSize / 2 && z > -fieldSize / 2 && z < fieldSize / 2) {
                    world.setBlockState(blockPos, Blocks.GLOWSTONE.getDefaultState());
                } else {
                    // ゲームフィールドの周りにあるブロックを溶岩に設定
                    world.setBlockState(blockPos, Blocks.LAVA.getDefaultState());
                }
            }
        }
    }

    //---------------------↓endGameに関連する処理---------------------

    private void endGame(PlayerEntity player) {
        // BGMの再生を停止
        customMusic.stop();

        // 変数をリセット
        reset();

        // プレイヤーの状態をリセット
        resetPlayerState(player);

        // ゲーム終了メッセージを表示
        player.sendStatusMessage(new StringTextComponent("Game Over! Score: " + score), false);

        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ServerWorld serverWorld = serverPlayer.getServerWorld();

            // 元の場所に戻る
            teleportPlayerToOriginalLocation(serverPlayer);

            // ゲームの時間をリセット
            resetWorldTime(serverWorld);

            // ゲーム終了後にMOBをすべてキルする
            removeAllMobs(serverWorld);

            // ドロップアイテムを削除
            removeAllItems(serverWorld, player);
        }

        // プレイヤーのインベントリをクリア
        clearPlayerInventory(player);

        // スコアボードを非表示にする
        hideScoreboard(player);

        // ランキング画面を表示
        showRankingScreen();
    }

    private void resetPlayerState(PlayerEntity player) {
        player.setHealth(player.getMaxHealth());
        player.getFoodStats().setFoodLevel(20);
    }

    private void teleportPlayerToOriginalLocation(ServerPlayerEntity serverPlayer) {
        serverPlayer.teleport(originalPlayerWorld, originalPlayerPos.getX(), originalPlayerPos.getY(), originalPlayerPos.getZ(), serverPlayer.rotationYaw, serverPlayer.rotationPitch);
    }

    private void resetWorldTime(ServerWorld serverWorld) {
        serverWorld.setDayTime(0);
    }

    private void removeAllMobs(ServerWorld serverWorld) {
        List<Entity> entities = serverWorld.getEntities().collect(Collectors.toList());
        for (Entity entity : entities) {
            if (entity instanceof MobEntity) {
                entity.remove();
            }
        }
    }

    private void removeAllItems(ServerWorld serverWorld, PlayerEntity player) {
        World world = player.getEntityWorld();
        List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, player.getBoundingBox().grow(10000.0D));
        for (ItemEntity item : items) {
            item.remove();
        }
    }

    private void showRankingScreen() {
        Minecraft.getInstance().displayGuiScreen(new NameInputScreen());
    }

    private void hideScoreboard(PlayerEntity player) {
        Scoreboard scoreboard = player.getEntityWorld().getScoreboard();
        String scoreName = "Survivors";
        ScoreObjective scoreObjective = scoreboard.getObjective(scoreName);
        scoreboard.setObjectiveInDisplaySlot(1, null);
        Score score = scoreboard.getOrCreateScore(player.getScoreboardName(), scoreObjective);
        score.setScorePoints(0);
    }

    private void clearPlayerInventory(PlayerEntity player) {
        // プレイヤーのメインインベントリとホットバーのアイテムをクリア
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
        }

        // プレイヤーの防具スロットのアイテムをクリア
        for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
            player.inventory.armorInventory.set(i, ItemStack.EMPTY);
        }

        // プレイヤーのオフハンドスロットのアイテムをクリア
        player.inventory.offHandInventory.set(0, ItemStack.EMPTY);
    }

    private void openNameInputScreen() {
        Minecraft.getInstance().enqueue(() -> {
            Minecraft.getInstance().displayGuiScreen(new NameInputScreen());
        });
    }

    //---------------------↑endGame処理関連-----------------------

    private void spawnEntities(World world, PlayerEntity player, int numEntities, double minRadius, double maxRadius,
                               float chickenChance, float redZombieChance, float childZombieChance, long delay) {

        Random random = new Random();

        for (int i = 0; i < numEntities; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double radius = minRadius + (maxRadius - minRadius) * random.nextDouble();
            double spawnX = player.getPosX() + radius * Math.cos(angle);
            double spawnZ = player.getPosZ() + radius * Math.sin(angle);
            BlockPos groundPos = world.getHeight(Heightmap.Type.WORLD_SURFACE, new BlockPos(spawnX, 0, spawnZ));

            if (random.nextFloat() < chickenChance) {
                ChickenEntity chicken = createChicken(world);
                chicken.setPosition(spawnX, groundPos.getY(), spawnZ);
                world.addEntity(chicken);
            } else if (random.nextFloat() < redZombieChance) {
                if (world.getGameTime() > delay) {
                    RedZombieEntity redZombie = createRedZombie(world);
                    redZombie.setPosition(spawnX, groundPos.getY(), spawnZ);
                    world.addEntity(redZombie);
                }
            } else {
                ZombieEntity zombie = createZombie(world, childZombieChance);
                zombie.setPosition(spawnX, groundPos.getY(), spawnZ);
                world.addEntity(zombie);
            }
        }
    }

    private ChickenEntity createChicken(World world) {
        ChickenEntity chicken = new ChickenEntity(EntityType.CHICKEN, world);
        chicken.setHealth(0.5F);
        return chicken;
    }

    private RedZombieEntity createRedZombie(World world) {
        RedZombieEntity redZombie = new RedZombieEntity(MobEntityTypes.RED_ZOMBIE.get(), world);
        return redZombie;
    }

    private ZombieEntity createZombie(World world, float childZombieChance) {
        ZombieEntity zombie = new ZombieEntity(EntityType.ZOMBIE, world);
        zombie.setHealth(0.5F);
        zombie.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.7D);

        if (new Random().nextFloat() < childZombieChance) {
            zombie.setChild(true);
            zombie.setHealth(0.5F);
            zombie.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D);
        }

        return zombie;
    }
}


