document.addEventListener('DOMContentLoaded', function () {
    const boldButton = document.getElementById('boldButton');
    const italicButton = document.getElementById('italicButton');
    const underlineButton = document.getElementById('underlineButton');

    const applyStyle = (command) => {
        document.execCommand(command, false, null);
    };

    // Use mousedown to prevent the editor from losing focus
    boldButton.addEventListener('mousedown', (e) => {
        e.preventDefault(); // Prevent button from taking focus
        applyStyle('bold');
    });

    italicButton.addEventListener('mousedown', (e) => {
        e.preventDefault();
        applyStyle('italic');
    });

    underlineButton.addEventListener('mousedown', (e) => {
        e.preventDefault();
        applyStyle('underline');
    });
});