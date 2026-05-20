package com.pulseforge.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Architecture guard tests using ArchUnit.
 *
 * <p>These tests enforce the dependency rules mandated by Clean Architecture:
 * the core must never depend on the infrastructure, and the delivery layer must
 * not reach directly into persistence internals.</p>
 */
@DisplayName("Clean Architecture Dependency Rules")
class CleanArchitectureTest {

    private static final String BASE_PACKAGE = "com.pulseforge";

    private static final String CORE_DOMAIN_PACKAGE      = BASE_PACKAGE + ".core.domain..";
    private static final String CORE_USECASES_PACKAGE    = BASE_PACKAGE + ".core.usecases..";
    private static final String INFRASTRUCTURE_PACKAGE   = BASE_PACKAGE + ".infrastructure..";
    private static final String DELIVERY_PACKAGE         = BASE_PACKAGE + ".infrastructure.delivery..";
    private static final String PERSISTENCE_PACKAGE      = BASE_PACKAGE + ".infrastructure.persistence..";
    private static final String CONFIGURATION_PACKAGE    = BASE_PACKAGE + ".infrastructure.configuration..";

    private static JavaClasses allClasses;

    @BeforeAll
    static void importClasses() {
        allClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE_PACKAGE);
    }

    @Test
    @DisplayName("Core domain must not import anything from infrastructure")
    void coreDomainMustNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(CORE_DOMAIN_PACKAGE)
                .should().dependOnClassesThat().resideInAPackage(INFRASTRUCTURE_PACKAGE)
                .because("the domain layer must remain completely framework-agnostic");

        rule.check(allClasses);
    }

    @Test
    @DisplayName("Core use cases must not import anything from infrastructure")
    void coreUseCasesMustNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(CORE_USECASES_PACKAGE)
                .should().dependOnClassesThat().resideInAPackage(INFRASTRUCTURE_PACKAGE)
                .because("use cases must depend only on domain objects and port interfaces");

        rule.check(allClasses);
    }

    @Test
    @DisplayName("Delivery layer must not import persistence internals directly")
    void deliveryMustNotDependOnPersistence() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(DELIVERY_PACKAGE)
                .should().dependOnClassesThat().resideInAPackage(PERSISTENCE_PACKAGE)
                .because("controllers must communicate with persistence only through use case ports");

        rule.check(allClasses);
    }

    @Test
    @DisplayName("Full layered architecture dependencies are respected")
    void layeredArchitectureDependenciesAreRespected() {
        ArchRule rule = layeredArchitecture()
                .consideringOnlyDependenciesInAnyPackage(BASE_PACKAGE + "..")
                .layer("Domain")        .definedBy(CORE_DOMAIN_PACKAGE)
                .layer("UseCases")      .definedBy(CORE_USECASES_PACKAGE)
                .layer("Delivery")      .definedBy(DELIVERY_PACKAGE)
                .layer("Persistence")   .definedBy(PERSISTENCE_PACKAGE)
                .layer("Configuration") .definedBy(CONFIGURATION_PACKAGE)
                .whereLayer("Domain")        .mayOnlyBeAccessedByLayers("UseCases", "Persistence", "Configuration", "Delivery")
                .whereLayer("UseCases")      .mayOnlyBeAccessedByLayers("Configuration", "Delivery")
                .whereLayer("Persistence")   .mayOnlyBeAccessedByLayers("Configuration")
                .whereLayer("Delivery")      .mayNotBeAccessedByAnyLayer()
                .because("Clean Architecture mandates a strict unidirectional dependency flow");

        rule.check(allClasses);
    }
}
