package com.alibaba.android.arouter.demo.testinterceptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.alibaba.android.arouter.demo.MainActivity;
import com.alibaba.android.arouter.demo.MainLooper;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.facade.template.IInterceptorTemporary;

/**
 * 一个 临时拦截器 的例子<br/>
 * 拦截器逻辑代码copy自 {@link Test1Interceptor}, 修改了以下部分：
 * <pre>
 *     1. 去除@Interceptor注解
 *     2. 实现的接口从 {@link IInterceptor} 改为 {@link IInterceptorTemporary}
 *     3. 在{@link #init(Context)} 方法中添加了注释说明
 *     4. 修改 {@link #process(Postcard, InterceptorCallback)} 方法中dialog的提示文字内容
 * </pre>
 * 临时拦截器使用注意事项：
 * <pre>
 *  1. 临时拦截器的接口为 {@link IInterceptorTemporary}
 *  2. 不要为临时拦截器配置@Interceptor注解
 *  3. 临时拦截器只对明确添加了该拦截器的路由调用生效，跟全局拦截器不同（注：1.4.1版之前的ARouter拦截器都是全局拦截器）
 *  4. 可以同时添加多个临时拦截器，按照添加的顺序执行
 *  5. 临时拦截器与全局拦截器不冲突，可同时使用在某次路由调用上，也可单独使用，如：
 *          ARouter.getInstance()
 *                 .build("/test/activity4")
 *                 .greenChannel() //本次路由禁用所有全局拦截器
 *                 .withInterceptor(new TestInterceptorTemporary()) //为本次路由添加临时拦截器
 *                 .navigation();
 *  6. 拦截器的执行顺序为：按从先到后的添加顺序执行临时拦截器 -> 按priority从小到大的顺序执行全局拦截器
 *  7. ARouter的自动注册方案中不会自动注册临时拦截器
 *  8. ARouter不会自动调用临时拦截器的初始化方法 {@link #init(Context)}，如果需要初始化，请自行在合适的时机初始化它
 * </pre>
 *
 * @author billy.qi <a href="https://github.com/luckybilly">Contact me.</a>
 * @since 2019/03/05 17:20
 */
public class TestInterceptorTemporary implements IInterceptorTemporary {
    Context mContext;

    /**
     * The operation of this interceptor.
     *
     * @param postcard meta
     * @param callback cb
     */

    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        if ("/test/activity4".equals(postcard.getPath())) {

            // 这里的弹窗仅做举例，代码写法不具有可参考价值
            final AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.getThis());
            ab.setCancelable(false);
            ab.setTitle("温馨提醒");
            ab.setMessage("想要跳转到Test4Activity么？\n\"临时\"拦截器\nTestInterceptorTemporary\n拦截了本次跳转");
            ab.setNegativeButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callback.onContinue(postcard);
                }
            });
            ab.setNeutralButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callback.onInterrupt(null);
                }
            });
            ab.setPositiveButton("加点料", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    postcard.withString("extra", "我是在拦截器中附加的参数");
                    callback.onContinue(postcard);
                }
            });

            MainLooper.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ab.create().show();
                }
            });
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        mContext = context;
        Log.e("testService", this.getClass().getName() + " has init.");
        //NOTICE：
        //  临时拦截器的init方法不会自动调用
        //  如果确实需要初始化，需要在使用前手动调用init方法
    }
}
