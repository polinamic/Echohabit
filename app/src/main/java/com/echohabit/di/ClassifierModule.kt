package com.echohabit.di

import android.content.Context
import com.echohabit.classifier.BehaviorClassifierOrchestrator
import com.echohabit.classifier.ml.TFLiteBehaviorClassifier
import com.echohabit.classifier.rules.BehaviorRuleEngine
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Qualifier for long-lived application coroutines.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

/**
 * Provides classifier dependencies for the app graph.
 */
@Module
@InstallIn(SingletonComponent::class)
object ClassifierModule {
    /**
     * Provides a reusable stateless rule engine.
     */
    @Provides
    @Reusable
    fun provideBehaviorRuleEngine(): BehaviorRuleEngine = BehaviorRuleEngine()

    /**
     * Provides a singleton TFLite classifier because the interpreter owns native resources.
     */
    @Provides
    @Singleton
    fun provideTFLiteBehaviorClassifier(
        @ApplicationContext context: Context
    ): TFLiteBehaviorClassifier = TFLiteBehaviorClassifier(context)

    /**
     * Provides the application-level classifier orchestrator.
     */
    @Provides
    @Singleton
    fun provideBehaviorClassifierOrchestrator(
        ruleEngine: BehaviorRuleEngine,
        tfliteClassifier: TFLiteBehaviorClassifier,
        @ApplicationScope scope: CoroutineScope
    ): BehaviorClassifierOrchestrator {
        return BehaviorClassifierOrchestrator(ruleEngine, tfliteClassifier, scope)
    }

    /**
     * Provides a process-wide coroutine scope for app services and flows.
     */
    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}
