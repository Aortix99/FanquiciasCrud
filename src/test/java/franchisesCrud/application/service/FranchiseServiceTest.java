package franchisesCrud.application.service;

import franchisesCrud.application.dto.interfac.BranchTopStockProjection;
import franchisesCrud.application.dto.request.CreateBranchRequest;
import franchisesCrud.application.dto.request.CreateFranchiseRequest;
import franchisesCrud.application.dto.request.UpdateNameRequest;
import franchisesCrud.application.dto.response.BranchResponse;
import franchisesCrud.application.dto.response.FranchiseResponse;
import franchisesCrud.application.dto.response.TopStockProductResponse;
import franchisesCrud.application.exception.NotFoundException;
import franchisesCrud.domain.model.Branch;
import franchisesCrud.domain.model.Franchise;
import franchisesCrud.infrastructure.repository.BranchRepository;
import franchisesCrud.infrastructure.repository.FranchiseRepository;
import franchisesCrud.infrastructure.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FranchiseService")
class FranchiseServiceTest {

    private static final Long FRANCHISE_ID = 10L;

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    private FranchiseService franchiseService;

    @BeforeEach
    void setUp() {
        franchiseService = new FranchiseService(franchiseRepository, branchRepository, productRepository);
    }

    @Nested
    @DisplayName("createFranchise")
    class CreateFranchise {

        @Test
        @DisplayName("persiste la franquicia con nombre recortado y devuelve la respuesta")
        void persistsTrimmedNameAndReturnsResponse() {
            CreateFranchiseRequest request = new CreateFranchiseRequest("  Mi Franquicia  ", "DOC-1");
            when(franchiseRepository.save(any(Franchise.class))).thenAnswer(invocation -> {
                Franchise f = invocation.getArgument(0);
                ReflectionTestUtils.setField(f, "id", 99L);
                return f;
            });

            FranchiseResponse result = franchiseService.createFranchise(request);

            assertThat(result.id()).isEqualTo(99L);
            assertThat(result.name()).isEqualTo("Mi Franquicia");

            ArgumentCaptor<Franchise> captor = ArgumentCaptor.forClass(Franchise.class);
            verify(franchiseRepository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Mi Franquicia");
        }
    }

    @Nested
    @DisplayName("addBranch")
    class AddBranch {

        @Test
        @DisplayName("lanza NotFoundException cuando la franquicia no existe")
        void throwsWhenFranchiseMissing() {
            when(franchiseRepository.findById(FRANCHISE_ID)).thenReturn(Optional.empty());
            CreateBranchRequest request = new CreateBranchRequest("Sucursal Centro");

            assertThatThrownBy(() -> franchiseService.addBranch(FRANCHISE_ID, request))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(String.valueOf(FRANCHISE_ID));

            verify(branchRepository, never()).save(any(Branch.class));
        }

        @Test
        @DisplayName("guarda la sucursal y devuelve BranchResponse con ids correctos")
        void savesBranchAndReturnsResponse() {
            Franchise franchise = new Franchise();
            ReflectionTestUtils.setField(franchise, "id", FRANCHISE_ID);
            franchise.setName("Franquicia X");
            when(franchiseRepository.findById(FRANCHISE_ID)).thenReturn(Optional.of(franchise));

            when(branchRepository.save(any(Branch.class))).thenAnswer(invocation -> {
                Branch b = invocation.getArgument(0);
                ReflectionTestUtils.setField(b, "id", 55L);
                return b;
            });

            BranchResponse result = franchiseService.addBranch(
                    FRANCHISE_ID,
                    new CreateBranchRequest("  Sucursal Norte  ")
            );

            assertThat(result.id()).isEqualTo(55L);
            assertThat(result.franchiseId()).isEqualTo(FRANCHISE_ID);
            assertThat(result.name()).isEqualTo("Sucursal Norte");

            ArgumentCaptor<Branch> captor = ArgumentCaptor.forClass(Branch.class);
            verify(branchRepository).save(captor.capture());
            assertThat(captor.getValue().getFranchise()).isSameAs(franchise);
            assertThat(captor.getValue().getName()).isEqualTo("Sucursal Norte");
        }
    }

    @Nested
    @DisplayName("updateFranchiseName")
    class UpdateFranchiseName {

        @Test
        @DisplayName("lanza NotFoundException cuando la franquicia no existe")
        void throwsWhenFranchiseMissing() {
            when(franchiseRepository.findById(FRANCHISE_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> franchiseService.updateFranchiseName(
                    FRANCHISE_ID,
                    new UpdateNameRequest("Nuevo nombre")
            ))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(String.valueOf(FRANCHISE_ID));

            verify(franchiseRepository, never()).save(any(Franchise.class));
        }

        @Test
        @DisplayName("actualiza el nombre y devuelve FranchiseResponse")
        void updatesNameAndReturnsResponse() {
            Franchise franchise = new Franchise();
            ReflectionTestUtils.setField(franchise, "id", FRANCHISE_ID);
            franchise.setName("Nombre viejo");
            when(franchiseRepository.findById(FRANCHISE_ID)).thenReturn(Optional.of(franchise));
            when(franchiseRepository.save(franchise)).thenReturn(franchise);

            FranchiseResponse result = franchiseService.updateFranchiseName(
                    FRANCHISE_ID,
                    new UpdateNameRequest("  Nuevo nombre legal  ")
            );

            assertThat(result.id()).isEqualTo(FRANCHISE_ID);
            assertThat(result.name()).isEqualTo("Nuevo nombre legal");
            verify(franchiseRepository).save(franchise);
        }
    }

    @Nested
    @DisplayName("findTopStockProductsPerBranch")
    class FindTopStockProductsPerBranch {

        @Test
        @DisplayName("lanza NotFoundException cuando la franquicia no existe")
        void throwsWhenFranchiseMissing() {
            when(franchiseRepository.existsById(FRANCHISE_ID)).thenReturn(false);

            assertThatThrownBy(() -> franchiseService.findTopStockProductsPerBranch(FRANCHISE_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(String.valueOf(FRANCHISE_ID));

            verify(productRepository, never()).findTopStockProductsByFranchise(FRANCHISE_ID);
        }

        @Test
        @DisplayName("devuelve lista mapeada desde el repositorio")
        void returnsMappedList() {
            when(franchiseRepository.existsById(FRANCHISE_ID)).thenReturn(true);
            BranchTopStockProjection row = new TestBranchTopStockProjection(
                    1L, "Sucursal A", 100L, "Producto X", 42
            );
            when(productRepository.findTopStockProductsByFranchise(FRANCHISE_ID)).thenReturn(List.of(row));

            List<TopStockProductResponse> result = franchiseService.findTopStockProductsPerBranch(FRANCHISE_ID);

            assertThat(result).hasSize(1);
            TopStockProductResponse first = result.get(0);
            assertThat(first.branchId()).isEqualTo(1L);
            assertThat(first.branchName()).isEqualTo("Sucursal A");
            assertThat(first.productId()).isEqualTo(100L);
            assertThat(first.productName()).isEqualTo("Producto X");
            assertThat(first.stock()).isEqualTo(42);

            verify(productRepository).findTopStockProductsByFranchise(FRANCHISE_ID);
        }
    }

    /**
     * Doble de prueba para la proyeccion nativa.
     */
    private record TestBranchTopStockProjection(
            Long branchId,
            String branchName,
            Long productId,
            String productName,
            Integer stock
    ) implements BranchTopStockProjection {

        @Override
        public Long getBranchId() {
            return branchId;
        }

        @Override
        public String getBranchName() {
            return branchName;
        }

        @Override
        public Long getProductId() {
            return productId;
        }

        @Override
        public String getProductName() {
            return productName;
        }

        @Override
        public Integer getStock() {
            return stock;
        }
    }
}
