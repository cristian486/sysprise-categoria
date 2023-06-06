package br.com.sysprise.categoria.service;

import br.com.sysprise.categoria.model.*;
import br.com.sysprise.categoria.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pb.CategoriaId;
import pb.ProdutoServiceGrpc;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GrpcClient("produto")
    private ProdutoServiceGrpc.ProdutoServiceBlockingStub produtoStub;

    public Categoria findCategoriaById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("A categoria requisitada não foi encontrada!"));
    }

    public Page<DadosListagemCategoria> listar(Pageable pageable) {
        return categoriaRepository.findAll(pageable).map(DadosListagemCategoria::new);
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public DadosDetalhamentoCategoria detalhar(Long id) {
        Categoria categoria = this.findCategoriaById(id);
        return new DadosDetalhamentoCategoria(categoria);
    }

    public DadosDetalhamentoCategoria cadastrar(DadosCadastroCategoria dadosCadastro) {
        Categoria categoria = new Categoria(dadosCadastro);
        categoriaRepository.save(categoria);
        return new DadosDetalhamentoCategoria(categoria);
    }

    public Long cadastrar(pb.CriarCategoriaRequest request) {
        Categoria categoria = new Categoria(request);
        categoriaRepository.save(categoria);
        return categoria.getId();
    }

    public DadosDetalhamentoCategoria atualizar(DadosAtualizarCategoria dadosAtualizar) {
        Categoria categoria = this.findCategoriaById(dadosAtualizar.id());
        categoria.atualizarCadastro(dadosAtualizar);
        return new DadosDetalhamentoCategoria(categoria);
    }

    public void deletar(Long id) {
        Categoria categoria = this.findCategoriaById(id);
        boolean haProdutosVinculados = produtoStub.verifyIfExistsProductsAssociatedWithCategory(CategoriaId.newBuilder().setId(id).build()).getExiste();
        if (haProdutosVinculados)
            throw new RuntimeException("Categoria não pode ser deletada pois há produtos vinculados a ela");
        categoriaRepository.delete(categoria);
    }
}
