package org.delcom.pam_p4_ifs23042.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.delcom.pam_p4_ifs23042.network.bags.service.BagAppContainer
import org.delcom.pam_p4_ifs23042.network.bags.service.IBagAppContainer
import org.delcom.pam_p4_ifs23042.network.bags.service.IBagRepository

@Module
@InstallIn(SingletonComponent::class)
object BagModule {
    @Provides
    fun provideBagContainer(): IBagAppContainer {
        return BagAppContainer()
    }

    @Provides
    fun provideBagRepository(container: IBagAppContainer): IBagRepository {
        return container.bagRepository
    }
}