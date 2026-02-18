import processing.core.PApplet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.lang.reflect.Constructor;

// =============================================================================
// 1. 設定と管理 (変更なし)
// =============================================================================
class ExpressionConfig {
    public final String name;
    public final String[] aliases;
    public final char keyBinding;
    public final Function<FacePartFactory, IExpressionState> stateFactory;

    public ExpressionConfig(String name, String[] aliases, char keyBinding,
                            Function<FacePartFactory, IExpressionState> stateFactory) {
        this.name = name;
        this.aliases = aliases;
        this.keyBinding = keyBinding;
        this.stateFactory = stateFactory;
    }
}

class ExpressionRegistry {
    private final Map<String, ExpressionConfig> expressionsByName = new HashMap<>();
    private final Map<java.lang.Character, ExpressionConfig> expressionsByKey = new HashMap<>();

    public void register(ExpressionConfig config) {
        if (config == null) return;
        expressionsByName.put(config.name.toLowerCase(), config);
        expressionsByKey.put(config.keyBinding, config);
        for (String alias : config.aliases) {
            expressionsByName.put(alias.toLowerCase(), config);
        }
    }
    public ExpressionConfig findByName(String name) { return expressionsByName.get(name.toLowerCase()); }
    public ExpressionConfig findByKey(char key) { return expressionsByKey.get(key); }
}

// =============================================================================
// 2. 自動化されたFactoryとBuilder (リファクタリングの中心)
// =============================================================================

/**
 * 【リファクタリング】リフレクションを使い、文字列の名前から動的にパーツを生成するFactory。
 */
class FacePartFactory {
    final PApplet app;
    final float s;

    FacePartFactory(PApplet app, float scale) { this.app = app; this.s = scale; }

    /**
     * 文字列で指定されたクラス名のパーツを生成する万能メソッド。
     * @param className 作成したいクラスの名前 (例: "BlinkingDecorator")
     * @param partToDecorate デコレータの場合に、装飾対象となるパーツ
     * @return 生成されたパーツのインスタンス。失敗した場合はEmptyPartを返す。
     */
    public UIComponent create(String className, UIComponent partToDecorate) {
        try {
            Class<?> partClass = Class.forName(className);
            // デコレータ用のコンストラクタを探す (引数がFacePartのもの)
            if (partToDecorate != null) {
                try {
                    Constructor<?> constructor = partClass.getConstructor(FacePart.class);
                    return (UIComponent) constructor.newInstance(partToDecorate);
                } catch (NoSuchMethodException e) {
                    // 見つからなければ次のコンストラクタを探しにいく
                }
            }
            // 通常パーツ/エフェクト用のコンストラクタを探す (引数がPApplet, floatのもの)
            Constructor<?> constructor = partClass.getConstructor(PApplet.class, float.class);
            return (UIComponent) constructor.newInstance(app, s);

        } catch (Exception e) {
            System.err.println("パーツの生成に失敗しました: " + className + ". クラス名やコンストラクタが正しいか確認してください。");
            //e.printStackTrace();
            return new EmptyPart(app, s);
        }
    }
}

/**
 * 【リファクタリング】switch文を撤廃し、命名規則に基づいてパーツを組み立てるBuilder。
 */
class ExpressionBuilder {
    private final FacePartFactory factory;
    private final CompositeFacePart face = new CompositeFacePart();

    public ExpressionBuilder(FacePartFactory factory) {
        this.factory = factory;
        this.withNose().withWhiskers();
    }

    // "blinking" -> "BlinkingDecorator" のように、ルールに従ってクラス名を生成するヘルパー
private String toClassName(String baseName, String suffix) {
    if (baseName == null || baseName.isEmpty()) return "";
    return java.lang.Character.toUpperCase(baseName.charAt(0))   // ← ここがポイント
         + baseName.substring(1)
         + suffix;
}

    public ExpressionBuilder eyes(String type) {
        UIComponent baseEyes = factory.create("Eyes", null);
        String decoratorName = toClassName(type, "Decorator");
        face.add(factory.create(decoratorName, baseEyes));
        return this;
    }

    public ExpressionBuilder ears(String type) {
        UIComponent baseEars = factory.create("Ears", null);
        String decoratorName = toClassName(type, "Decorator");
        face.add(factory.create(decoratorName, baseEars));
        return this;
    }

    public ExpressionBuilder mouth(String type) {
        face.add(new Mouth(factory.app, factory.s, type));
        return this;
    }

    public ExpressionBuilder effect(String type) {
        String className = toClassName(type, "Decorator");
        face.add(factory.create(className, null));
        return this;
    }

    public ExpressionBuilder withNose() {
        face.add(factory.create("Nose", null));
        return this;
    }
    public ExpressionBuilder withWhiskers() {
        face.add(factory.create("Whiskers", null));
        return this;
    }

    public IExpressionState build() {
        return new IExpressionState() {
            public void update() { face.update(); }
            public void draw() { face.draw(); }
        };
    }
}

// =============================================================================
// 3. 本体 (Characterクラス)
// =============================================================================
class Character {
    private IExpressionState currentState;
    private ExpressionConfig currentConfig;
    final FacePartFactory factory;
    final ExpressionRegistry registry;

    public Character(PApplet app, float scale) {
        this.factory = new FacePartFactory(app, scale);
        this.registry = new ExpressionRegistry();
        initializeExpressions();
        setExpression("NORMAL");
    }

    private void initializeExpressions() {
        registry.register(new ExpressionConfig(
            "NORMAL", new String[]{"normal", "ノーマル", "通常"}, '1',
            f -> new ExpressionBuilder(f)
                .eyes("blinking")
                .ears("earTwitch")
                .mouth("normal")
                .build()
        ));
        
        registry.register(new ExpressionConfig(
            "QUESTION", new String[]{"question", "疑問", "thinking"}, '2',
            f -> new ExpressionBuilder(f)
                .eyes("blinking")
                .ears("earTwitch")
                .mouth("happy")
                .effect("questionMark")
                .build()
        ));

        registry.register(new ExpressionConfig(
            "HAPPY", new String[]{"happy", "嬉しい"}, '3',
            f -> new ExpressionBuilder(f)
                .eyes("sparkle")
                .ears("earFlap")
                .mouth("surprised")
                .build()
        ));

        registry.register(new ExpressionConfig(
            "SLEEPING", new String[]{"sleeping", "sleep", "寝る"}, '4',
            f -> new ExpressionBuilder(f)
                .eyes("sleepEyes")
                .ears("earDroop")
                .effect("zzz") 
                .build() 
        ));

        registry.register(new ExpressionConfig(
            "SMILE", new String[]{"smile", "笑顔"}, '5',
            f -> new ExpressionBuilder(f)
                .eyes("NikoEyes")
                .ears("earFlap")
                .mouth("nikoSmile")
                .build()
        ));

    }

    /**
     * 実行時に新しい表情を動的に追加します。
     * @param config 追加する表情の設定
     */
    public void addExpression(ExpressionConfig config) {
        registry.register(config);
    }

    /**
     * 文字列で表情を変更します。
     * @param name 表情の正式名または別名
     */
    public void setExpression(String name) {
        if (name == null) return;
        ExpressionConfig config = registry.findByName(name.trim());
        if (config != null) {
            setExpression(config);
        }
    }

    /**
     * 設定オブジェクトに基づいて表情を内部的に変更します。
     * @param config 適用する表情設定
     */
    private void setExpression(ExpressionConfig config) {
        if (config == null || config == currentConfig) return;
        currentState = config.stateFactory.apply(factory);
        currentConfig = config;
    }

    /**
     * 現在の表情の正式名を取得します。
     * @return 現在の表情名
     */
    public String getCurrentExpressionName() {
        return currentConfig != null ? currentConfig.name : "UNKNOWN";
    }

    /**
     * 現在の表情状態を更新します。
     */
    public void update() {
        if (currentState != null) {
            currentState.update();
        }
    }

    /**
     * 現在の表情状態を描画します。
     */
    public void draw() {
        if (currentState != null) {
            currentState.draw();
        }
    }
}

// =============================================================================
// 4. インターフェース (変更なし)
// =============================================================================
interface IExpressionState {
    void update();
    void draw();
}
