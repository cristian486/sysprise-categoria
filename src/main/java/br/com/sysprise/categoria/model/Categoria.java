package br.com.sysprise.categoria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "nome")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;

    public Categoria(pb.CriarCategoriaRequest request) {
        this.nome = request.getNome();
        this.descricao = request.getDescricao();
    }

    public Categoria(DadosCadastroCategoria dadosCadastro) {
        this.nome = dadosCadastro.nome();
        this.descricao = dadosCadastro.descricao();
    }

    public void atualizarCadastro(DadosAtualizarCategoria dadosAtualizar) {
        if(dadosAtualizar.nome() != null && !dadosAtualizar.nome().isEmpty())
            this.nome = dadosAtualizar.nome();

        if(dadosAtualizar.descricao() != null && !dadosAtualizar.descricao().isEmpty())
            this.descricao = dadosAtualizar.descricao();
    }
}
