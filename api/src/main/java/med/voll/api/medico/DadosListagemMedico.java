package med.voll.api.medico;

public record DadosListagemMedico(
        Long id,
        String nome,
        String email,
        String crm,
        Especialidade especialidade
) {

    //CONSTRUTOR PARA USAR NA CLASSE MedicoController NO MÉTODO LISTAR
    public DadosListagemMedico(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade());
    }
}
