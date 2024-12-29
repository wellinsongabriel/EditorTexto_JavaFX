package application;

public enum Tema {
	CLARO(	
			"-fx-background-color: #ffffff;",//cor da borda, precisa ser definida
			"-fx-control-inner-background: #ffffff;",// //cor do fundo sem essa propriedade não é difinido corretamente
			"-fx-text-fill: #000000; ",//cor do texto
			"-fx-highlight-fill: #1a5f8c;", //cor da seleção
			"-fx-highlight-text-fill: #ffffff;"//cor texto selecionado
			), //cor do texto selecionado
	ESCURO(
			"-fx-background-color: #282a36;",//cor da borda, precisa ser definida
			"-fx-control-inner-background: #282a36;",// sem essa propriedade não é difinido corretamente
			"-fx-text-fill: #ffffff; ", //cor do texto
			"-fx-highlight-fill: #666b87;", //cor da seleção
			"-fx-highlight-text-fill: #ffffff;"
			); //cor do texto selecionado
	
	String corBorda;
	String corFundo; 
	String corTexto; 
	String corSelecao;
	String corTextoSelecionado;

	private Tema(String corBorda, String corFundo, String corTexto, String corSelecao, String corTextoSelecionado) {
		this.corBorda = corBorda;
		this.corFundo = corFundo;
		this.corTexto = corTexto;
		this.corSelecao = corSelecao;
		this.corTextoSelecionado = corTextoSelecionado;
	}
	
	
	public static String modoEscuro() {
		return ESCURO.corBorda+ESCURO.corFundo+ESCURO.corTexto+ESCURO.corSelecao+ESCURO.corTextoSelecionado;
	}
	
	public static String modoClaro() {
		return CLARO.corBorda+CLARO.corFundo+CLARO.corTexto+CLARO.corSelecao+CLARO.corTextoSelecionado;
	}
	

}
