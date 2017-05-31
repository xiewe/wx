function stopBubble(e) { 
if ( e && e.stopPropagation ) 
    e.stopPropagation(); 
else
    window.event.cancelBubble = true; 
}

function stopDefault( e ) { 
    if ( e && e.preventDefault ) 
        e.preventDefault(); 
    else
        window.event.returnValue = false; 
    return false; 
}
