package eu.f3rog.blade.weaving;

import com.github.stephanenicolas.afterburner.AfterBurner;

import java.util.Arrays;
import java.util.List;

import eu.f3rog.blade.weaving.util.AWeaver;
import eu.f3rog.blade.weaving.util.WeavingUtil;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.build.JavassistBuildException;

import static eu.f3rog.blade.weaving.util.WeavingUtil.isSubclassOf;

public class BladeWeaver extends AWeaver {

    private interface Class {
        // android classes
        String FRAGMENT = "android.app.Fragment";
        String SUPPORT_FRAGMENT = "android.support.v4.app.Fragment";
        // android classes
        String ACTIVITY = "android.app.Activity";
        String APP_COMPAT_ACTIVITY = "android.support.v7.app.AppCompatActivity";
        // generated classes
        String INJECTOR = "blade.Injector";
    }

    private interface Method {
        // android classes
        String ON_CREATE = "onCreate";
        String ON_ATTACH = "onAttach";
        // generated classes
        String INJECT = "inject";
    }

    private static final List<String> REQUIRED_CLASSES = Arrays.asList(
            Class.INJECTOR
    );

    private AfterBurner mAfterBurner;

    /**
     * Constructor
     */
    public BladeWeaver(boolean debug) {
        super(REQUIRED_CLASSES, debug);
        mAfterBurner = new AfterBurner();
    }

    @Override
    public boolean needTransformation(CtClass candidateClass) throws JavassistBuildException {
        try {
            //log("needTransformation ? %s", candidateClass.getName());
            return WeavingUtil.isSubclassOf(candidateClass,
                    Class.ACTIVITY,
                    Class.APP_COMPAT_ACTIVITY,
                    Class.FRAGMENT,
                    Class.SUPPORT_FRAGMENT);
        } catch (Exception e) {
            log("needTransformation failed on class %s", candidateClass.getName());
            e.printStackTrace();
            throw new JavassistBuildException(e);
        }
    }

    @Override
    public void applyTransformations(CtClass classToTransform) throws JavassistBuildException {
        log("Applying transformation to %s", classToTransform.getName());
        try {
            // ACTIVITY
            if (WeavingUtil.isSubclassOf(classToTransform, Class.ACTIVITY, Class.APP_COMPAT_ACTIVITY)) {
                weaveActivity(classToTransform);
            }
            // FRAGMENT
            else if (WeavingUtil.isSubclassOf(classToTransform, Class.FRAGMENT, Class.SUPPORT_FRAGMENT)) {
                weaveFragment(classToTransform);
            }
            // nothing done
            else {
                log("Nothing changed");
            }
            log("Transformation done");
        } catch (Exception e) {
            log("Transformation failed!");
            e.printStackTrace();
            throw new JavassistBuildException(e);
        }
    }

    private boolean isSupported(CtClass classToTransform) {
        try {
            classToTransform.getClassPool().get(Class.INJECTOR).getDeclaredMethod(Method.INJECT, new CtClass[]{classToTransform});
            return true;
        } catch (NotFoundException e) {
            //log("No inject() method");
            return false;
        }
    }

    private void weaveActivity(CtClass classToTransform) throws Exception {
        if (!isSupported(classToTransform)) return;

        String body = String.format("{ %s.%s(this); }", Class.INJECTOR, Method.INJECT);
        // weave into method
        mAfterBurner.beforeOverrideMethod(classToTransform, Method.ON_CREATE, body);
        log("%s weaved into %s", body, Method.ON_CREATE);
    }

    private void weaveFragment(CtClass classToTransform) throws Exception {
        if (!isSupported(classToTransform)) return;

        String body = String.format("{ %s.%s(this); }", Class.INJECTOR, Method.INJECT);
        // weave into method
        mAfterBurner.beforeOverrideMethod(classToTransform, Method.ON_ATTACH, body);
        log("%s weaved into %s", body, Method.ON_ATTACH);
    }

}