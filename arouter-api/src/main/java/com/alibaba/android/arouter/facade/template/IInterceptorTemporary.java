package com.alibaba.android.arouter.facade.template;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Interceptor;

/**
 * Used for inject custom logic when navigation.<br/>
 * The difference from the IInterceptor interface is that:<br/>
 *  {@link IInterceptorTemporary} is temporary interceptor and only work for the route which emit with this interceptor
 *      <pre>
 *        ARouter.getInstance().build(url)
 *          .greenChannel()                  //disable all global interceptors (optional)
 *          .withInterceptor(interceptor1)   //add 1st temporary interceptor
 *          .withInterceptor(interceptor2)   //add 2nd temporary interceptor (optional)
 *          .navigation();
 *      </pre>
 *  {@link IInterceptor} is global interceptor.<br/>
 *
 *  Note:<br/>
 *      1. {@link Interceptor} Annotation is not allowed<br/>
 *      2. {@link IInterceptorTemporary#init(Context)} will not be called by ARouter.
 *              If needed, please call it manually before use
 *      3. If there are multiple temporary interceptors, ARouter will execute them in the order they were added
 *      4. Temporary interceptors will execute before all global interceptors.
 *
 *
 * @author billy.qi <a href="mailto:qiyilike@163.com">Contact me.</a>
 * @since 19/03/06 15:16
 */
public interface IInterceptorTemporary extends IInterceptor {
}
