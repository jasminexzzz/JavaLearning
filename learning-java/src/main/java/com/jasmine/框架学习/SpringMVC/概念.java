package com.jasmine.框架学习.SpringMVC;

/**
 * @author : jasmineXz
 */
public class 概念 {
    /**
     @link https://www.cnblogs.com/liwangcai/p/10743943.html


     一 .
     FrameworkServlet.service方法,继承与HttpServlet
         @Override
         protected void service(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
             //HttpMethod为枚举类型，成员为GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
             HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
             if (method.equalsIgnoreCase(RequestMethod.PATCH.name())) {　　　　　　  //如果请求方法为空，或者是PATCH
                processRequest(request, response);                                  //处理请求
             } else {
                super.service(request, response);//交给父类进行处理,本例中我们是get请求，所以转到HttpServlet.servlet
             }
         }

     二.
         HttpServlet.servlet,调用doGet请求
         protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
             String method = req.getMethod();
             long lastModified;
             if (method.equals("GET")) {
                 lastModified = this.getLastModified(req);
                 if (lastModified == -1L) {
                    this.doGet(req, resp); // 调用doGet请求
                 } else {
                     long ifModifiedSince = req.getDateHeader("If-Modified-Since");
                     if (ifModifiedSince < lastModified) {
                         this.maybeSetLastModified(resp, lastModified);
                         this.doGet(req, resp);
                     } else {
                        resp.setStatus(304);
                     }
                 }
             } else if (method.equals("HEAD")) {
                 lastModified = this.getLastModified(req);
                 this.maybeSetLastModified(resp, lastModified);
                 this.doHead(req, resp);
             } else if (method.equals("POST")) {
                this.doPost(req, resp);
             } else if (method.equals("PUT")) {
                this.doPut(req, resp);
             } else if (method.equals("DELETE")) {
                this.doDelete(req, resp);
             } else if (method.equals("OPTIONS")) {
                this.doOptions(req, resp);
             } else if (method.equals("TRACE")) {
                this.doTrace(req, resp);
             } else {
                String errMsg = lStrings.getString("http.method_not_implemented");
                Object[] errArgs = new Object[]{method};
                errMsg = MessageFormat.format(errMsg, errArgs);
                resp.sendError(501, errMsg);
             }
         }

     三. FrameworkServlet.doGet方法,FrameworkServlet有重写doGet方法,所以调用
         @Override
         protected final void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
            processRequest(request, response);
         }

     四.

         protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
             long startTime = System.currentTimeMillis();
             Throwable failureCause = null;

             //拿到之前的LocaleContext上下文（因为可能在Filter里已经设置过了）
             LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
             // 以当前的request创建一个Local的上下文，后面会继续处理
             LocaleContext localeContext = buildLocaleContext(request);

             // 持有上下文的Request容器
             RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
             // 这里面build逻辑注意：previousAttributes若为null，或者就是ServletRequestAttributes类型，那就new ServletRequestAttributes(request, response);
             // 若不为null，就保持之前的绑定结果，不再做重复绑定了（尊重原创）
             ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes);

             // 拿到异步管理器。这里是首次获取，会new WebAsyncManager(),然后放到request的attr里面
             WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
             //这里需要注意：给异步上下文恒定注册了RequestBindingInterceptor这个拦截器（作用：绑定当前的request、response、local等）
             asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor());

             //这句话很明显，就是吧request和Local上下文、RequestContext绑定
             initContextHolders(request, localeContext, requestAttributes);

             try {
                //模版设计模式：由子类DispatcherServlet去实现实际逻辑
                doService(request, response);
             } catch (ServletException ex) {
                 ....
             } finally {
                 //这个时候已经全部处理完成，视图已经渲染了
                 //doService()方法完成后，重置上下文，也就是解绑
                 resetContextHolders(request, previousLocaleContext, previousAttributes);
                 if (requestAttributes != null) {
                    requestAttributes.requestCompleted();
                 }

                 if (logger.isDebugEnabled()) {
                     if (failureCause != null) {
                        this.logger.debug("Could not complete request", failureCause);
                     }
                     else {
                         if (asyncManager.isConcurrentHandlingStarted()) {
                            logger.debug("Leaving response open for concurrent processing");
                         }
                         else {
                            this.logger.debug("Successfully completed request");
                         }
                     }
                 }
                //关键：不管执行成功与否，都会发布一个事件，说我处理了这个请求（有需要监听的，就可以监听这个事件了，每次请求都会有）
                 publishRequestHandledEvent(request, response, startTime, failureCause);
             }
         }

     五. DispatcherServlet.doService
         @Override
         protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
             if (logger.isDebugEnabled()) {
                 String resumed = WebAsyncUtils.getAsyncManager(request).hasConcurrentResult() ? " resumed" : "";
                 logger.debug("DispatcherServlet with name '" + getServletName() + "'" + resumed +
                 " processing " + request.getMethod() + " request for [" + getRequestUri(request) + "]");
             }

             // 如果该请求是include的请求（请求包含） 那么就把request域中的数据保存一份快照版本
             // 等doDispatch结束之后，会把这个快照版本的数据覆盖到新的request里面去
             Map<String, Object> attributesSnapshot = null;
             if (WebUtils.isIncludeRequest(request)) {
                 attributesSnapshot = new HashMap<String, Object>();
                 Enumeration<?> attrNames = request.getAttributeNames();
                 while (attrNames.hasMoreElements()) {
                     String attrName = (String) attrNames.nextElement();
                     if (this.cleanupAfterInclude || attrName.startsWith("org.springframework.web.servlet")) {
                        attributesSnapshot.put(attrName, request.getAttribute(attrName));
                     }
                 }
             }

             // Make framework objects available to handlers and view objects.
             // 说得很清楚，把一些常用对象放进请求域  方便Handler里面可以随意获取
             request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
             request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
             request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
             request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());

             // 如果是重定向，放置得更多一些~~~~
             FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response);
             if (inputFlashMap != null) {
                request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
             }
             request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
             request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);

             try {
                 // DispatcherServlet最重要的方法，交给他去分发请求你、找到handler处理等等
                doDispatch(request, response);
             } finally {
                 if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
                     // Restore the original attribute snapshot, in case of an include.
                     // 如果是include请求  会上上面的数据快照，重新放置到request里面去
                     if (attributesSnapshot != null) {
                        restoreAttributesAfterInclude(request, attributesSnapshot);
                     }
                 }
             }
         }

     六. DispatcherServlet.doDispatch
         作为DispatcherServlet的核心,用于处理映射

         protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
             HttpServletRequest processedRequest = request;
             HandlerExecutionChain mappedHandler = null;
             boolean multipartRequestParsed = false;

             WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

             try {
                 ModelAndView mv = null;
                 Exception dispatchException = null;

                 try {
                     //如果请求是POST请求，并且请求头中的Context-Type是以multipart/开头的就认为是文件上传的请求
                     processedRequest = checkMultipart(request);
                     // 标记一下：是否是文件上传的request了
                     multipartRequestParsed = (processedRequest != request);

                     // 找到一个处理器，如果没有找到对应的处理类的话，这里通常会返回404，如果throwExceptionIfNoHandlerFound属性值为true的情况下会抛出异常
                     mappedHandler = getHandler(processedRequest);
                     if (mappedHandler == null || mappedHandler.getHandler() == null) {
                         noHandlerFound(processedRequest, response);
                         return;
                     }

                     // Determine handler adapter for the current request.
                     HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

                     // Process last-modified header, if supported by the handler.
                     String method = request.getMethod();
                     boolean isGet = "GET".equals(method);
                     if (isGet || "HEAD".equals(method)) {
                         long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                         if (logger.isDebugEnabled()) {
                            logger.debug("Last-Modified value for [" + getRequestUri(request) + "] is: " + lastModified);
                         }
                         if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
                            return;
                         }
                     }

                     if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                         return;
                     }
                     // Actually invoke the handler.
                     mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
                     if (asyncManager.isConcurrentHandlingStarted()) {
                         return;
                     }
                     applyDefaultViewName(request, mv);
                     mappedHandler.applyPostHandle(processedRequest, response, mv);
                 } catch (Exception ex) {
                     dispatchException = ex;
                 }
                 processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
             } catch (Exception ex) {
                 ...
             }
             finally {
                 if (asyncManager.isConcurrentHandlingStarted()) {
                     // Instead of postHandle and afterCompletion
                     if (mappedHandler != null) {
                        mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
                     }
                 }
                 else {
                     // Clean up any resources used by a multipart request.
                     if (multipartRequestParsed) {
                        cleanupMultipart(processedRequest);
                     }
                 }
             }
         }

     七. checkMultipart

     八. DispatcherServlet.getHandler

         protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
             // 会把配置的所有的HandlerMapping 都拿出来查找，只要找到一个就返回
             for (HandlerMapping hm : this.handlerMappings) {
                 if (logger.isTraceEnabled()) {
                     logger.trace(
                     "Testing handler map [" + hm + "] in DispatcherServlet with name '" + getServletName() + "'");
                 }
                 HandlerExecutionChain handler = hm.getHandler(request);
                 if (handler != null) {
                    return handler;
                 }
             }
             return null;
         }

         SpringMVC默认加载三个请求处理映射类：RequestMappingHandlerMapping、SimpleUrlHandlerMapping、和BeanNameUrlHandlerMapping。
         这三个类有一个共同的父类：AbstractHandlerMapping。在上面代码中hm.getHandler(request)这个getHandler方法在AbstractHandlerMapping中，
         它的子类都没有重写这个方法。因此我们含有必要去AbstractHandlerMapping这个类中看一下这个方法：
     */
}
