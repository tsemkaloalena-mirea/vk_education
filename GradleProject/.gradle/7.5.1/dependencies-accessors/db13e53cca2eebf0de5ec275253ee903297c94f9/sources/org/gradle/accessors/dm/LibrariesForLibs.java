package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
*/
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final JunitLibraryAccessors laccForJunitLibraryAccessors = new JunitLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(providers, config);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers) {
        super(config, providers);
    }

        /**
         * Creates a dependency provider for annotations (com.intellij:annotations)
         * This dependency was declared in settings file 'settings.gradle.kts'
         */
        public Provider<MinimalExternalModuleDependency> getAnnotations() { return create("annotations"); }

        /**
         * Creates a dependency provider for lombok (org.projectlombok:lombok)
         * This dependency was declared in settings file 'settings.gradle.kts'
         */
        public Provider<MinimalExternalModuleDependency> getLombok() { return create("lombok"); }

    /**
     * Returns the group of libraries at junit
     */
    public JunitLibraryAccessors getJunit() { return laccForJunitLibraryAccessors; }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() { return paccForPluginAccessors; }

    public static class JunitLibraryAccessors extends SubDependencyFactory {
        private final JunitTestLibraryAccessors laccForJunitTestLibraryAccessors = new JunitTestLibraryAccessors(owner);

        public JunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at junit.test
         */
        public JunitTestLibraryAccessors getTest() { return laccForJunitTestLibraryAccessors; }

    }

    public static class JunitTestLibraryAccessors extends SubDependencyFactory {

        public JunitTestLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (org.junit.jupiter:junit-jupiter-api)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getApi() { return create("junit.test.api"); }

            /**
             * Creates a dependency provider for engine (org.junit.jupiter:junit-jupiter-engine)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getEngine() { return create("junit.test.engine"); }

    }

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: junit (5.9.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in settings file 'settings.gradle.kts'
             */
            public Provider<String> getJunit() { return getVersion("junit"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
