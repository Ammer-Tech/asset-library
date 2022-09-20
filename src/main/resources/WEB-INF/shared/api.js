async function submitCreateNetwork()
{
    var data = {};
    data["id"] = document.getElementsByName("id")[0].value;
    data["networkId"] = document.getElementsByName("networkId")[0].value;
    data["networkName"] = document.getElementsByName("networkName")[0].value;
    data["visibleName"] = document.getElementsByName("visibleName")[0].value;
    data["iconUrl"] = document.getElementsByName("iconUrl")[0].value;
    data["explorerTransactions"] = document.getElementsByName("explorerTransactions")[0].value;
    data["explorerAddress"] = document.getElementsByName("explorerAddress")[0].value;
    data["compatiblePubKeys"] = document.getElementsByName("compatiblePubKeys")[0].value;
    data["visible"] = document.getElementsByName("visible")[0].checked;
    const options = {method: 'POST',body: JSON.stringify(data),headers: { 'Content-Type':'application/json' }}
    var res = await fetch('/createNetwork', options);
    if(res.status !== 200){
        alert('The server returned an error - could not save the network!');
        return;
    }
    var json = await res.json()
    alert('Successfully saved the new network!')
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

async function getAssetByIdAndType()
{
    //Get a specific asset.
    var res = await fetch();
}

async function getNetworkById()
{
    //Get a specific asset.
    var res = await fetch();
}


async function listNetworks()
{
    const options = {method: 'GET'}
    var res = await fetch('/api/public/networks', options);
    if(res.status !== 200){
        alert('Could not fetch the list of networks!');
        return;
    }
    var json = await res.json()
    alert(json)
}

async function listAssets()
{
    //Use fetch api to get the list of all networks.
    var res = await fetch();
}
