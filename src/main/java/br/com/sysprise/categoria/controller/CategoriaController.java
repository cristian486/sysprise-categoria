package br.com.sysprise.categoria.controller;

import br.com.sysprise.categoria.model.*;
import br.com.sysprise.categoria.service.CategoriaService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;

@RestController
@RequestMapping("/categoria")
@CrossOrigin
@AllArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemCategoria>> listar(@PageableDefault(sort = "id", size = 5) Pageable pageable) {
        Page<DadosListagemCategoria> dadosListagem = categoriaService.listar(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(dadosListagem);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoCategoria> detalhar(@PathVariable("id") Long id) {
        DadosDetalhamentoCategoria dadosDetalhamento = categoriaService.detalhar(id);
        return ResponseEntity.status(HttpStatus.OK).body(dadosDetalhamento);
    }


    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoCategoria> cadastrar(@RequestBody @Valid DadosCadastroCategoria dadosCadastro, UriComponentsBuilder componentsBuilder) {
        DadosDetalhamentoCategoria dadosDetalhamento = categoriaService.cadastrar(dadosCadastro);
        URI uri = componentsBuilder.path("/categoria/{id}").buildAndExpand(dadosDetalhamento.id()).toUri();
        return ResponseEntity.created(uri).body(dadosDetalhamento);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoCategoria> atualizar(@RequestBody @Valid DadosAtualizarCategoria dadosAtualizar) {
        DadosDetalhamentoCategoria dadosDetalhamento = categoriaService.atualizar(dadosAtualizar);
        return ResponseEntity.status(HttpStatus.OK).body(dadosDetalhamento);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
