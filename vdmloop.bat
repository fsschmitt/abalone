@echo off

set S1=Movimento.rtf
set S2=Casa.rtf
set S3=Jogada.rtf
set S4=Jogo.rtf
set S5=Jogador.rtf
set S6=Tabuleiro.rtf
set S7=TestJogo.rtf
set S8=TestJogada.rtf

"C:\Program Files (x86)\The VDM++ Toolbox Academic v8.0\bin\vppde" -p -R vdm.tc %S1% %S2% %S3% %S4% %S5% %S6% %S7% %S8%
for /R %%f in (*.arg) do call vdmtest "%%f"
