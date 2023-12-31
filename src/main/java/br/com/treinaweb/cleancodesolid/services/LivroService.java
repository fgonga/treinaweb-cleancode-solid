package br.com.treinaweb.cleancodesolid.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.treinaweb.cleancodesolid.dtos.inputs.LivroRequest;
import br.com.treinaweb.cleancodesolid.dtos.outputs.LivroResponse;
import br.com.treinaweb.cleancodesolid.exceptions.ValidacaoException;
import br.com.treinaweb.cleancodesolid.models.Livro;
import br.com.treinaweb.cleancodesolid.repositories.LivroRepository;

@Service
public class LivroService {

    private LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public LivroResponse cadastrar(LivroRequest livroRequest) {
        // Converte um LivroResponse em um Livro
        Livro livro = new Livro();
        livro.setTitulo(livroRequest.getTitulo());
        livro.setAutor(livroRequest.getAutor());
        livro.setPaginas(livroRequest.getPaginas());
        livro.setIsbn(livroRequest.getIsbn());
        livro.setDescricao(livroRequest.getDescricao());

        // Aplica regras de validação no Livro
        if (livro.getTitulo() == null) {
            throw new ValidacaoException("O título não pode ser nulo");
        }

        if (livro.getTitulo().isEmpty()) {
            throw new ValidacaoException("O título não pode estar em branco");
        }

        if (livro.getTitulo().length() < 3) {
            throw new ValidacaoException("O título não pode ter menos que 3 caracteres");
        }

        if (livro.getTitulo().length() > 255) {
            throw new ValidacaoException("O título não pode ter mais que 255 caracteres");
        }

        if (livroRepository.isTituloExists(livro.getTitulo())) {
            throw new ValidacaoException("Já existe um livro cadastrado com esse título");
        }

        if (livro.getIsbn() != null && livro.getIsbn().length() != 10) {
            throw new ValidacaoException("O ISBN deve ter 10 caracteres");
        }

        // Salva livro no banco de dados
        livroRepository.save(livro);

        // Converte um Livro em um LivroResponse
        LivroResponse livroResponse = new LivroResponse();
        livroResponse.setId(livro.getId());
        livroResponse.setTitulo(livro.getTitulo());
        livroResponse.setAutor(livro.getAutor());

        // Retorna o LivroResponse
        return livroResponse;
    }

    public List<LivroResponse> listar() {
        List<Livro> livros = livroRepository.findAll();

        // Converte Livro em LivroResponse
        List<LivroResponse> livroResponseList = new ArrayList<>();
        for (Livro livro : livros) {
            LivroResponse livroResponse = new LivroResponse();
            livroResponse.setId(livro.getId());
            livroResponse.setTitulo(livro.getTitulo());
            livroResponse.setAutor(livro.getAutor());

            livroResponseList.add(livroResponse);
        }

        // Retorna a lista de LivroResponse
        return livroResponseList;
    }

}
