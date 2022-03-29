function judge(AA,BB,CC){
    for(ii=0;ii<AA.length;ii++){
        var aa = AA[ii];
        var bb = BB[ii];
        var tt = (aa+"="+bb);
        eval(tt);
    }
	return eval(CC);
}