package edu.uoc.epcsd.user.domain.service;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.properties.HasName;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import edu.uoc.epcsd.user.application.rest.response.GetUserResponse;
import edu.uoc.epcsd.user.domain.User;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = "edu.uoc.epcsd.user"
        , importOptions = ImportOption.DoNotIncludeTests.class)
public class UserArchUnitTest {

    @ArchTest
    static final ArchRule onionArchitectureIsRespected =
            onionArchitecture()
                    .domainModels("..domain")
                    .domainServices("..domain.service", "..domain.repository")
                    // Tot i que no tenim application services en el nostre cas, ja que és una aplicació molt petita
                    // afegim els serveis de domini per cumplir la onion architecture
                    .applicationServices("..domain.service")
                    .adapter("rest", "..application.rest")
                    .adapter("persistence", "..infrastructure.repository.jpa", "..infrastructure.repository.rest")
                    // En aquest cas s'està incomplint l'onion architecture estrictament, però hi ha casos on es permeten
                    // aquest tipus de dependències, com per exemple per traduïr de Dominio a un adaptador
                    // donat que no volem modificar el codi font, ignorem la dependència.
                    .ignoreDependency(GetUserResponse.class, User.class);

    @ArchTest
    static final ArchRule servicesImplementationsEndWithImpl =
            ArchRuleDefinition.classes().that().resideInAPackage("edu.uoc.epcsd.user.domain.service").and()
                    .areAnnotatedWith(Service.class)
                    .should().haveSimpleNameEndingWith("Impl");

}
