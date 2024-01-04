package med.voll.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.DadosDetalhamentoPaciente;
import med.voll.api.domain.paciente.DadosListagemPaciente;
import med.voll.api.domain.paciente.DadosUpDatePaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriComponentsBuilder) {
        var paciente = new Paciente(dados);
        pacienteRepository.save(paciente);
        var uri = uriComponentsBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPaciente>> listar(@PageableDefault Pageable pagina) {
        var page = pacienteRepository.findAllByAtivoTrue(pagina).map(DadosListagemPaciente::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity upDate(@RequestBody @Valid DadosUpDatePaciente dados) {
        var paciente = pacienteRepository.getReferenceById(dados.id());
        paciente.upDateInformacoesPaciente(dados);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.excluir();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity recuperar(@PathVariable Long id, DadosUpDatePaciente dados) {
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.recuperar();
        paciente.upDateInformacoesPaciente(dados);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var paciente = pacienteRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }
}
