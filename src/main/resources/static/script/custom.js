/*
  window.addEventListener('load', (event) => {
      console.log('The page has fully loaded');
       let iframeNo = 0;
       for (const iframe of document.documentElement.querySelectorAll('iframe')) {
           iframe.src='/iframe/' + iframeNo;
           iframeNo++;
        }

  });
*/
  document.addEventListener('blur', (e) => {
     console.log(e);

    const { key, target } = e
    console.log(key);
    console.log(target);
 })
 document.addEventListener('change', (e) => {
       var xpath =getElementXPath(event.target);
      let field = {};
      field.xpath = xpath;
      field.value = event.target.value;
      sendField(field);
   /*
     alert(event.target.value);
     alert(event.target.id);
     console.log(e);

    const { key, target } = e

    console.log(key);
    console.log(target);
    console.log(this);
    console.log(this.value);
    alert(this.value);

    alert(target.name);
    */
 })




  document.addEventListener('click', (e) => {
     //alert("click");
     //alert(event.target.value);
     //alert(event.target.id);
    // alert(target.name);
     console.log(e);

    const { key, target } = e
    console.log(key);

    if(target.type === 'button' ){
      var xpath =getElementXPath(event.target);
       let field = {};
       field.xpath = xpath;
       sendClick(field);;
    }
    //alert(target.type);
 })
 /*   if (target === url) {
      return
    } else if (target === input) {
      if ((key !== 'Process' && key !== 'Enter') || (key === 'Process' && (e.code === 'ArrowLeft' || e.code === 'ArrowRight'))) {
        window.setTimeout(
          () => window.requestAnimationFrame(
            () => composition(target)
          ),
          5
        )
      }

      if (key !== 'Enter') {
        return
      }
    } else if (['Backspace'].includes(key)) {
      e.preventDefault()
    }

   })
*/
/*
  document.addEventListener('keydown', (e) => {
     console.log(e);

    const { key, target } = e
    console.log(key);
    console.log(target);
 })
*/
/*
    if (target === url) {
      return
    } else if (target === input) {
      if ((key !== 'Process' && key !== 'Enter') || (key === 'Process' && (e.code === 'ArrowLeft' || e.code === 'ArrowRight'))) {
        window.setTimeout(
          () => window.requestAnimationFrame(
            () => composition(target)
          ),
          5
        )
      }

      if (key !== 'Enter') {
        return
      }
    } else if (['Backspace'].includes(key)) {
      e.preventDefault()
    }

   })
*/

  document.addEventListener('keyup', (e) => {
     console.log(e);

    const { key, target } = e
    console.log(key);
    console.log(target);
 })

/*
    if (target === url) {
      return
    } else if (target === input) {
      if (key !== 'Enter') {
        return
      }
    } else if (['Backspace'].includes(key)) {
      e.preventDefault()
    }

   })
*/

/**
 * Gets an XPath for an element which describes its hierarchical location.
 */
function getElementXPath(element)
{
  if (element && element.id)
    return '//*[@id="' + element.id + '"]';
  else
    return getElementTreeXPath(element);
};

function getElementTreeXPath(element)
{
  var paths = [];

  // Use nodeName (instead of localName) so namespace prefix is included (if any).
  for (; element && element.nodeType == 1; element = element.parentNode)
  {
    var index = 0;
    for (var sibling = element.previousSibling; sibling; sibling = sibling.previousSibling)
    {
      // Ignore document type declaration.
      if (sibling.nodeType == Node.DOCUMENT_TYPE_NODE)
        continue;

      if (sibling.nodeName == element.nodeName)
        ++index;
    }

    var tagName = element.nodeName.toLowerCase();
    var pathIndex = (index ? "[" + (index+1) + "]" : "");
    paths.splice(0, 0, tagName + pathIndex);
  }

  return paths.length ? "/" + paths.join("/") : null;
};


function sendField(field){// pass your data in method
     $.ajax({
             type: "POST",
             url: window.location.protocol + '//'+ window.location.hostname  + ':' +  window.location.port + "/updateFieldData",
             data: JSON.stringify(field),// now data come in this function
             contentType: "application/json; charset=utf-8",
             crossDomain: true,
             dataType: "json",
             success: function (field, status, jqXHR) {

                // alert("success");// write success in " "
             },

             error: function (jqXHR, status) {
                 // error handler
                 //console.log(jqXHR);
                // alert('fail' + status.code);
             }
          });
    }

    function sendClick(field){// pass your data in method

         $.ajax({
                 type: "POST",
                 url: window.location.protocol + '//'+ window.location.hostname  + ':' +  window.location.port+ "/click",
                 data: JSON.stringify(field),// now data come in this function
                 contentType: "application/json; charset=utf-8",
                 crossDomain: true,
                 dataType: "json",
                 success: function (data, status, jqXHR) {
                       setTimeout(()=> {
                          window.location.href = data.url;
                       }
                       ,3000);


                    // alert("success");// write success in " "
                 },

                 error: function (jqXHR, status) {
                     // error handler
                     console.log(jqXHR);
                     alert('fail' + status.code);

                 }
              });
        }