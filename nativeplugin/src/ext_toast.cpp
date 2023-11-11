// Extension lib defines
#define EXTENSION_NAME androidInput
#define LIB_NAME "androidInput"
#define MODULE_NAME LIB_NAME


// Defold SDK
#define DLIB_LOG_DOMAIN LIB_NAME
#include <dmsdk/sdk.h>

#if defined(DM_PLATFORM_ANDROID)

#include <android/log.h>

//#include "testlib.h" // Multiply
static JNIEnv* g_env;
static jobject g_obj;

static jclass g_GamepadClass= 0;
static jmethodID g_GetGamepadMethodId = 0;
static jobject		g_GamepadObject = 0;

static JNIEnv* Attach()
{
    JNIEnv* env;
    JavaVM* vm = dmGraphics::GetNativeAndroidJavaVM();
    vm->AttachCurrentThread(&env, NULL);
    return env;
}

static bool Detach(JNIEnv* env)
{
    bool exception = (bool) env->ExceptionCheck();
    env->ExceptionClear();
    JavaVM* vm = dmGraphics::GetNativeAndroidJavaVM();
    vm->DetachCurrentThread();
    return !exception;
}

namespace 
{
    struct AttachScope
    {
        JNIEnv* m_Env;
        AttachScope() : m_Env(Attach())
        {
        }
        ~AttachScope()
        {
            Detach(m_Env);
        }
    };
}

static jclass GetClass(JNIEnv* env, const char* classname)
{
    jclass activity_class = env->FindClass("android/app/NativeActivity");
    jmethodID get_class_loader = env->GetMethodID(activity_class,"getClassLoader", "()Ljava/lang/ClassLoader;");
    jobject cls = env->CallObjectMethod(dmGraphics::GetNativeAndroidActivity(), get_class_loader);
    jclass class_loader = env->FindClass("java/lang/ClassLoader");
    jmethodID find_class = env->GetMethodID(class_loader, "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");

    jstring str_class_name = env->NewStringUTF(classname);
    jclass outcls = (jclass)env->CallObjectMethod(cls, find_class, str_class_name);
    env->DeleteLocalRef(str_class_name);
    return outcls;
}

static int InitializeAndroidGamepad(lua_State* L)
{
    dmLogInfo("Trying to initialize defold Android Gamepad");
    AttachScope attachscope;
    JNIEnv* env = attachscope.m_Env;
    //get the Gamepad Input Class
    jclass tmp = GetClass(env, "com.defold.android.gamepad/AndroidGamepad");
    g_GamepadClass = (jclass)env-> NewGlobalRef(tmp);
    if(!g_GamepadClass)
    {
        dmLogError("Could not find class 'com.defold.android.gamepad/AndroidGamepad'");
        return false;
    }
    dmLogInfo("Class Android Gamepad loaded");

    g_GetGamepadMethodId = env->GetStaticMethodID(g_GamepadClass, "getGamepad", "(Landroid/content/Context;)Lcom/defold/android/gamepad/AndroidGamepad;");
    if(!g_GetGamepadMethodId)
    {
        dmLogError("Could not get static method 'getGamepad'.");
        return false;
    }
    dmLogInfo("Get Gamepad method is also loaded");

    jobject tmp1 = env->CallStaticObjectMethod(g_GamepadClass, g_GetGamepadMethodId, dmGraphics::GetNativeAndroidActivity());
    g_GamepadObject = (jobject) env -> NewGlobalRef(tmp1);
    if(!g_GamepadObject)    
    {
        dmLogError("Could not create instance.");
        return false;
    }

    
    
    return 1;
}

static int DoStuffJava(lua_State* L)
{
    return 1;
}

void logMessage(const char* tag, const char* message) 
{
    __android_log_print(ANDROID_LOG_INFO, tag, "%s", message);
}

static int ToastMessage(lua_State* L)
{    
    AttachScope attachscope;
    JNIEnv* env = attachscope.m_Env;

    const char* message = luaL_checkstring(L, 1);
    int duration = luaL_optinteger(L, 2, 0);  // Default duration to 0 if not provided
    // Ensure duration is between 0 and 1
    duration = (duration > 1) ? 1 : ((duration < 0) ? 0 : duration);

    // Get the class
    jclass cls = GetClass(env, "com.defold.toastextension.MyToast");
    if (cls == nullptr) {
        return luaL_error(L, "Class not found");
        //return 1;
    }
    logMessage("TAG_PLUGIN", "Class Loaded");
    
    // Get the method ID
    jmethodID showToastMethod = env->GetStaticMethodID(cls, "showToast", "(Landroid/content/Context;Ljava/lang/String;I)V");
    if (showToastMethod == nullptr) {
        return luaL_error(L, "Method not found");        
    }
    logMessage("TAG_PLUGIN", "Method Loaded");
    
    // Get the context
    jobject context = dmGraphics::GetNativeAndroidActivity();

    logMessage("TAG_PLUGIN", "Context Loaded");

    // Call the static method
    env->CallStaticVoidMethod(cls, showToastMethod, context, env->NewStringUTF(message), duration);    
    return 1;
}

// Functions exposed to Lua
static const luaL_reg Module_methods[] =
{
    {"dostuff_java", DoStuffJava},
    {"toast", ToastMessage},
    {"initGamepad", InitializeAndroidGamepad },
    //{"dostuff_jar", DoStuffJar},
    //{"vibrate", Vibrate},
    //{"getraw", GetRaw},
    //{"multiply", Multiply},
    {0, 0}
};

static void LuaInit(lua_State* L)
{
    int top = lua_gettop(L);

    // Register lua names
    luaL_register(L, MODULE_NAME, Module_methods);

    lua_pop(L, 1);
    assert(top == lua_gettop(L));
}

static dmExtension::Result AppInitializeExtension(dmExtension::AppParams* params)
{    
    return dmExtension::RESULT_OK;
}

static dmExtension::Result InitializeExtension(dmExtension::Params* params)
{
    // Init Lua
    LuaInit(params->m_L);
    printf("Registered %s Extension\n", MODULE_NAME);    
    return dmExtension::RESULT_OK;
}

static dmExtension::Result AppFinalizeExtension(dmExtension::AppParams* params)
{
    logMessage("TAG_PLUGIN", "Gamepad Plugin Here");    
    return dmExtension::RESULT_OK;
}

static dmExtension::Result FinalizeExtension(dmExtension::Params* params)
{
    return dmExtension::RESULT_OK;
}

#else //Not supported Platforms
static dmExtension::Result AppInitializeExtension(dmExtension::AppParams* params)
{
    dmLogWarning("Registered %s (null) Extension\n", MODULE_NAME);
    return dmExtension::RESULT_OK;
}

static dmExtension::Result InitializeExtension(dmExtension::Params* params)
{    
    return dmExtension::RESULT_OK;
}

static dmExtension::Result AppFinalizeExtension(dmExtension::AppParams* params)
{
    return dmExtension::RESULT_OK;
}

static dmExtension::Result FinalizeExtension(dmExtension::Params* params)
{
    return dmExtension::RESULT_OK;
}

#endif

DM_DECLARE_EXTENSION(EXTENSION_NAME, LIB_NAME, AppInitializeExtension, AppFinalizeExtension, InitializeExtension, 0, 0, FinalizeExtension)
