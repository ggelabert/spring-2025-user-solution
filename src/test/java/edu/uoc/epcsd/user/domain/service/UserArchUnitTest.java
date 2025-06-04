package edu.uoc.epcsd.user.domain.service;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.properties.HasName;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import edu.uoc.epcsd.user.application.rest.response.GetUserResponse;
import edu.uoc.epcsd.user.domain.*;
import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.AlertEntity;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.DigitalItemEntity;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.DigitalSessionEntity;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.UserEntity;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = "edu.uoc.epcsd.user"
        , importOptions = ImportOption.DoNotIncludeTests.class)
public class UserArchUnitTest {

    private static final DescribedPredicate<Dependency> implementsDomainTranslatable = DescribedPredicate.describe("implements DomainTranslatable", dependency -> {
        return dependency.getOriginClass().getInterfaces().stream().anyMatch(type -> type.equals(HasName.Predicates.name("DomainTranslatable")));
    });

    private static final DescribedPredicate<Dependency> isModelClass = DescribedPredicate.describe("is from domain model", dependency -> {
        return dependency.getTargetClass().getPackage().getRelativeName().equals("domain");
    });

    @ArchTest
    static final ArchRule onionArchitectureIsRespected =
            onionArchitecture()
                    .domainModels("..domain")
                    .domainServices("..domain.service", "..domain.repository")
                    .applicationServices("..domain.service")
                    .adapter("rest", "..application.rest")
                    .adapter("persistence", "..infrastructure.repository.jpa", "..infrastructure.repository.rest")
                    .ignoreDependency(GetUserResponse.class, User.class);

    @ArchTest
    static final ArchRule servicesImplementationsEndWithImpl =
            ArchRuleDefinition.classes().that().resideInAPackage("edu.uoc.epcsd.user.domain.service").and()
                    .areAnnotatedWith(Service.class)
                    .should().haveSimpleNameEndingWith("Impl");

}
