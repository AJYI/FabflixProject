function getParameterByName(target) {
  // Get request URL
  let url = window.location.href;
  // Encode target parameter name to url encoding
  target = target.replace(/[\[\]]/g, "\\$&");

  // Ues regular expression to find matched parameter value
  let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
    results = regex.exec(url);
  if (!results) return null; // Changed from null
  if (!results[2]) return "";

  // Return the decoded parameter value
  return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function sortClick(sortConfig) {
  let sortParam1 = getParameterByName("sort1");
  let sortParam2 = getParameterByName("sort2");

  // First time
  if (sortParam1 == null) {
    if (sortConfig === "TA") {
      window.location.href += "&sort1=TA&sort2=NA";
    } else if (sortConfig === "TD") {
      window.location.href += "&sort1=TD&sort2=NA";
    } else if (sortConfig === "RA") {
      window.location.href += "&sort1=RA&sort2=NA";
    } else if (sortConfig === "RD"){
      window.location.href += "&sort1=RD&sort2=NA";
    }
    console.log("First time reached the end");
  }

  else if(sortParam1 == "null"){
    let strReplace = sortParam1;
    let url = window.location.href
    window.location.href = url.replace(strReplace, sortConfig);
  }

  // Checking whether we are updating parameter1 again
  else if(sortParam1.charAt(0) == sortConfig.charAt(0)){
    let strReplace = sortParam1;
    let url = window.location.href
    console.log(strReplace + " " + url);
    window.location.href = url.replace(strReplace, sortConfig);
  }

  else if (sortParam2 == "NA"){
    let url = window.location.href
    let strReplace = "NA";
    if (sortConfig === "TA") {
      window.location.href = url.replace(strReplace, "TA");
    } else if (sortConfig === "TD") {
      window.location.href = url.replace(strReplace, "TD");
    } else if (sortConfig === "RA") {
      window.location.href = url.replace(strReplace, "RA");
    } else if (sortConfig === "RD"){
      window.location.href = url.replace(strReplace, "RD");
    }
    console.log("Second time reached the end");
  }

  else if(sortParam2 == "null"){
    let strReplace = sortParam2;
    let url = window.location.href
    window.location.href = url.replace(strReplace, sortConfig);
  }

  // Checking whether we are updating parameter1 again
  else if(sortParam2.charAt(0) == sortConfig.charAt(0)){
    let strReplace = sortParam2;
    let url = window.location.href
    window.location.href = url.replace(strReplace, sortConfig);
  }

  console.log("made it here");
  //   alert("The URL of this page is: " + window.location.href);
  //     window.location.href = "";
}
