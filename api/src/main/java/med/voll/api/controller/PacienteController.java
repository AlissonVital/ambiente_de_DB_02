package med.voll.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import med.voll.api.paciente.DadosCadastroPaciente;
import med.voll.api.paciente.DadosListagemPaciente;
import med.voll.api.paciente.DadosUpDatePaciente;
import med.voll.api.paciente.Paciente;
import med.voll.api.paciente.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroPaciente dados) {
        pacienteRepository.save(new Paciente(dados));
        System.out.println();
        System.out.println(dados);
    }

    @GetMapping
    public Page<DadosListagemPaciente> listar(@PageableDefault Pageable pagina) {
        return pacienteRepository.findAllByAtivoTrue(pagina).map(DadosListagemPaciente::new);
    }

    @PutMapping
    @Transactional
    public void upDate(@RequestBody @Valid DadosUpDatePaciente dados) {
        var paciente = pacienteRepository.getReferenceById(dados.id());
        paciente.upDateInformacoesPaciente(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable Long id) {
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.excluir();
    }

    @PutMapping("/{id}")
    @Transactional
    public void recuperar(@PathVariable Long id) {
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.recuperar();
    }
}
