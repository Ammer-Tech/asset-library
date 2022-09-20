async function submitCreateNetwork()
{
    var data = new FormData();
    data.append("email",document.getElementById("networKname"));
    //continued.
    var res = await fetch();
    //Check for 200 and then get the result and pain the canvas.
    //If not 200 then show error dialog.
}

async function submitCreateAsset()
{
    var data = new FormData();
    data.append("email",document.getElementById("networKname"));
    //continued.
    //if ERC20 then ignore the sub-tokens.
    //if ERC721/ERC1155 then squash and over-ride each one of them with the "new" values, and create
    //the ones that are missing.
    var res = await fetch();
    //Check for 200 and then get the result and pain the canvas.
    //If not 200 then show error dialog.
}


async function listNetworks()
{
    //Use fetch api to get the list of all networks.
    var res = await fetch();
}

async function listAssets()
{
    //Use fetch api to get the list of all networks.
    var res = await fetch();
}
