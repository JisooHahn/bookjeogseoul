const myProfileService = (() => {
    // 프사 생성 또는 수정
    const setProfileImage = async (file) => {
        const formData = new FormData();
        formData.append("file", file);

        const response = await fetch(`/sponsor/upload-profile`, {
            method: "POST",
            body: formData
        });

        if (!response.ok) throw response;
        return await response.text();
    };

    // 프사 삭제
    const deleteProfileImage = async () => {
        const response = await fetch(`/sponsor/delete-profile`, {
            method : "DELETE",
            headers: {
                "Content-Type": "application/json;charset-utf-8"
            }
        });

        if (!response.ok) throw response;
    }

    // 아이디 변경
    const setSponsorId = async (sponsorId) => {
        const response = await fetch(`/sponsor/sponsor-id`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `sponsorId=${encodeURIComponent(sponsorId)}`
        });
        if(!response.ok) throw response;
        return await response.text();
    }

    // 단체명 변경
    const setSponsorName = async (sponsorName) => {
        const response = await fetch(`/sponsor/sponsor-name`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `sponsorName=${encodeURIComponent(sponsorName)}`
        });
        if (!response.ok) throw response;
        return await response.text();
    };

    // 비밀번호 변경
    const setPassword = async (currentPassword, newPassword) => {
        const response = await fetch(`/sponsor/password`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `currentPassword=${encodeURIComponent(currentPassword)}&newPassword=${encodeURIComponent(newPassword)}`
        });

        if (!response.ok) throw response;
        return await response.text();
    };

    return { setProfileImage : setProfileImage, deleteProfileImage : deleteProfileImage,
        setSponsorName : setSponsorName, setPassword : setPassword, setSponsorId : setSponsorId
    }
})();