package eu.f3rog.blade.compiler.extra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.junit.Test;

import javax.tools.JavaFileObject;

import blade.Blade;
import blade.Extra;
import eu.f3rog.blade.compiler.BaseTest;
import eu.f3rog.blade.compiler.BladeProcessor;
import eu.f3rog.blade.core.BundleWrapper;
import eu.f3rog.blade.core.GeneratedFor;

import static eu.f3rog.blade.compiler.util.File.file;
import static eu.f3rog.blade.compiler.util.File.generatedFile;

/**
 * Class {@link IntentManagerTest}
 *
 * @author FrantisekGazo
 * @version 2015-11-27
 */
public final class IntentManagerTest extends BaseTest {

    @Test
    public void activityNone() {
        JavaFileObject input = file("com.example", "SomeActivity")
                .imports(
                        Blade.class, "B",
                        Activity.class
                )
                .body(
                        "@$B",
                        "public class $T extends Activity {}"
                );

        JavaFileObject expected = generatedFile("blade", "I")
                .imports(
                        GeneratedFor.class, "GF",
                        input, "A",
                        BundleWrapper.class, "BW",
                        Intent.class,
                        Context.class
                )
                .body(
                        "public class $T {",
                        "",
                        "   @$GF($A.class)",
                        "   public static Intent for$A(Context context) {",
                        "       Intent intent = new Intent(context, $A.class);",
                        "       $BW extras = new $BW();",
                        "       intent.putExtras(extras.getBundle());",
                        "       return intent;",
                        "   }",
                        "",
                        "   @$GF($A.class)",
                        "   public static void start$A(Context context) {",
                        "       context.startActivity(for$A(context));",
                        "   }",
                        "",
                        "}"
                );

        assertFiles(input)
                .with(BladeProcessor.Module.EXTRA)
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void activityOne() {
        JavaFileObject input = file("com.example", "SomeActivity")
                .imports(
                        Extra.class, "E",
                        Activity.class
                )
                .body(
                        "public class $T extends Activity {",
                        "",
                        "   @$E String mText;",
                        "",
                        "}"
                );

        JavaFileObject expected = generatedFile("blade", "I")
                .imports(
                        GeneratedFor.class, "GF",
                        input, "A",
                        BundleWrapper.class, "BW",
                        String.class,
                        Intent.class,
                        Context.class
                )
                .body(
                        "public class $T {",
                        "",
                        "   @$GF($A.class)",
                        "   public static Intent for$A(Context context, String mText) {",
                        "       Intent intent = new Intent(context, $A.class);",
                        "       $BW extras = new $BW();",
                        "       extras.put(\"<Extra-mText>\", mText);",
                        "       intent.putExtras(extras.getBundle());",
                        "       return intent;",
                        "   }",
                        "",
                        "   @$GF($A.class)",
                        "   public static void start$A(Context context, String mText) {",
                        "       context.startActivity(for$A(context, mText));",
                        "   }",
                        "",
                        "}"
                );

        assertFiles(input)
                .with(BladeProcessor.Module.EXTRA)
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void activityMore() {
        JavaFileObject input1 = file("com.example", "FirstActivity")
                .imports(
                        Extra.class, "E",
                        Activity.class
                )
                .body(
                        "public class $T extends Activity {",
                        "",
                        "   @$E int number;",
                        "",
                        "}"
                );
        JavaFileObject input2 = file("com.example", "SecondActivity")
                .imports(
                        Extra.class, "E",
                        Activity.class
                )
                .body(
                        "public class $T extends Activity {",
                        "",
                        "   @$E String text;",
                        "   @$E boolean flag;",
                        "   @$E double number;",
                        "",
                        "}"
                );

        JavaFileObject expected = generatedFile("blade", "I")
                .imports(
                        GeneratedFor.class, "GF",
                        input1, "A1",
                        input2, "A2",
                        BundleWrapper.class, "BW",
                        String.class,
                        Intent.class,
                        Context.class
                )
                .body(
                        "public class $T {",
                        "",
                        "   @$GF($A1.class)",
                        "   public static Intent for$A1(Context context, int number) {",
                        "       Intent intent = new Intent(context, $A1.class);",
                        "       $BW extras = new $BW();",
                        "       extras.put(\"<Extra-number>\", number);",
                        "       intent.putExtras(extras.getBundle());",
                        "       return intent;",
                        "   }",
                        "",
                        "   @$GF($A1.class)",
                        "   public static void start$A1(Context context, int number) {",
                        "       context.startActivity(for$A1(context, number));",
                        "   }",
                        "",
                        "   @$GF($A2.class)",
                        "   public static Intent for$A2(Context context, String text, boolean flag, double number) {",
                        "       Intent intent = new Intent(context, $A2.class);",
                        "       $BW extras = new $BW();",
                        "       extras.put(\"<Extra-text>\", text);",
                        "       extras.put(\"<Extra-flag>\", flag);",
                        "       extras.put(\"<Extra-number>\", number);",
                        "       intent.putExtras(extras.getBundle());",
                        "       return intent;",
                        "   }",
                        "",
                        "   @$GF($A2.class)",
                        "   public static void start$A2(Context context, String text, boolean flag, double number) {",
                        "       context.startActivity(for$A2(context, text, flag, number));",
                        "   }",
                        "",
                        "}"
                );

        assertFiles(input1, input2)
                .with(BladeProcessor.Module.EXTRA)
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void activityInheritance() {
        JavaFileObject base = file("com.example", "BaseActivity")
                .imports(
                        Extra.class, "E",
                        Activity.class
                )
                .body(
                        "public class $T extends Activity {",
                        "",
                        "   @$E int number;",
                        "",
                        "}"
                );
        JavaFileObject activity = file("com.example", "MyActivity")
                .imports(
                        Extra.class, "E",
                        base, "B"
                )
                .body(
                        "public class $T extends $B {",
                        "",
                        "   @$E String text;",
                        "",
                        "}"
                );

        JavaFileObject expected = generatedFile("blade", "I")
                .imports(
                        GeneratedFor.class, "GF",
                        base, "B",
                        activity, "A",
                        BundleWrapper.class, "BW",
                        String.class,
                        Intent.class,
                        Context.class
                )
                .body(
                        "public class $T {",
                        "",
                        "   @$GF($B.class)",
                        "   public static Intent for$B(Context context, int number) {",
                        "       Intent intent = new Intent(context, $B.class);",
                        "       $BW extras = new $BW();",
                        "       extras.put(\"<Extra-number>\", number);",
                        "       intent.putExtras(extras.getBundle());",
                        "       return intent;",
                        "   }",
                        "",
                        "   @$GF($B.class)",
                        "   public static void start$B(Context context, int number) {",
                        "       context.startActivity(for$B(context, number));",
                        "   }",
                        "",
                        "   @$GF($A.class)",
                        "   public static Intent for$A(Context context, int number, String text) {",
                        "       Intent intent = new Intent(context, $A.class);",
                        "       $BW extras = new $BW();",
                        "       extras.put(\"<Extra-number>\", number);",
                        "       extras.put(\"<Extra-text>\", text);",
                        "       intent.putExtras(extras.getBundle());",
                        "       return intent;",
                        "   }",
                        "",
                        "   @$GF($A.class)",
                        "   public static void start$A(Context context, int number, String text) {",
                        "       context.startActivity(for$A(context, number, text));",
                        "   }",
                        "",
                        "}"
                );

        assertFiles(base, activity)
                .with(BladeProcessor.Module.EXTRA)
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void activityInheritanceFromAbstract() {
        JavaFileObject base = file("com.example", "BaseActivity")
                .imports(
                        Extra.class, "E",
                        Activity.class
                )
                .body(
                        "public abstract class $T extends Activity {",
                        "",
                        "   @$E int number;",
                        "",
                        "}"
                );
        JavaFileObject activity = file("com.example", "MyActivity")
                .imports(
                        Extra.class, "E",
                        base, "B"
                )
                .body(
                        "public class $T extends $B {",
                        "",
                        "   @$E String text;",
                        "",
                        "}"
                );

        JavaFileObject expected = generatedFile("blade", "I")
                .imports(
                        GeneratedFor.class, "GF",
                        activity, "A",
                        BundleWrapper.class, "BW",
                        String.class,
                        Intent.class,
                        Context.class
                )
                .body(
                        "public class $T {",
                        "",
                        "   @$GF($A.class)",
                        "   public static Intent for$A(Context context, int number, String text) {",
                        "       Intent intent = new Intent(context, $A.class);",
                        "       $BW extras = new $BW();",
                        "       extras.put(\"<Extra-number>\", number);",
                        "       extras.put(\"<Extra-text>\", text);",
                        "       intent.putExtras(extras.getBundle());",
                        "       return intent;",
                        "   }",
                        "",
                        "   @$GF($A.class)",
                        "   public static void start$A(Context context, int number, String text) {",
                        "       context.startActivity(for$A(context, number, text));",
                        "   }",
                        "",
                        "}"
                );

        assertFiles(base, activity)
                .with(BladeProcessor.Module.EXTRA)
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

}
