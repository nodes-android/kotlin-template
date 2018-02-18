package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.managers.ResourceManager
import dk.eboks.app.domain.repositories.AppStateRepository
import dk.eboks.app.storage.managers.AppStateManagerImpl
import dk.eboks.app.storage.managers.FileCacheManagerImpl
import dk.eboks.app.system.managers.PrefManagerImpl
import dk.eboks.app.system.managers.ResourceManagerImpl
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 25-07-2017.
 */
@Module
class StorageModule {
    @Provides
    @AppScope
    fun providePrefManager(context: Context) : PrefManager
    {
        return PrefManagerImpl(context)
    }

    @Provides
    @AppScope
    fun provideResourceManager(context: Context) : ResourceManager
    {
        return ResourceManagerImpl(context)
    }

    @Provides
    @AppScope
    fun provideAppStateManager(appStateRepository: AppStateRepository) : AppStateManager
    {
        return AppStateManagerImpl(appStateRepository)
    }

    @Provides
    @AppScope
    fun provideFileCacheManager(context: Context, gson: Gson) : FileCacheManager
    {
        return FileCacheManagerImpl(context, gson)
    }
}